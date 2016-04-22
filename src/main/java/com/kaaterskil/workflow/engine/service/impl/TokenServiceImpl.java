package com.kaaterskil.workflow.engine.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kaaterskil.workflow.engine.context.Context;
import com.kaaterskil.workflow.engine.delegate.event.WorkflowEventDispatcher;
import com.kaaterskil.workflow.engine.delegate.event.WorkflowEventFactory;
import com.kaaterskil.workflow.engine.delegate.event.WorkflowEventType;
import com.kaaterskil.workflow.engine.exception.EntityNotFoundException;
import com.kaaterskil.workflow.engine.persistence.entity.EventSubscription;
import com.kaaterskil.workflow.engine.persistence.entity.JobEntity;
import com.kaaterskil.workflow.engine.persistence.entity.ProcessDefinitionEntity;
import com.kaaterskil.workflow.engine.persistence.entity.Token;
import com.kaaterskil.workflow.engine.persistence.entity.VariableEntity;
import com.kaaterskil.workflow.engine.persistence.repository.TokenRepository;
import com.kaaterskil.workflow.engine.service.EventSubscriptionService;
import com.kaaterskil.workflow.engine.service.JobService;
import com.kaaterskil.workflow.engine.service.TokenService;
import com.kaaterskil.workflow.engine.service.VariableService;
import com.kaaterskil.workflow.engine.util.tree.TokenTree;
import com.kaaterskil.workflow.engine.util.tree.TokenTreeNode;
import com.kaaterskil.workflow.engine.util.tree.TokenTreeUtil;
import com.kaaterskil.workflow.util.CollectionUtil;

@Component
public class TokenServiceImpl implements TokenService {

    @Autowired
    private TokenRepository repository;

    @Override
    public Token findById(Long tokenId) {
        return repository.findOne(tokenId);
    }

    @Override
    public TokenTree findTokenTree(Long processInstanceId) {
        return TokenTreeUtil.buildTree(repository.findByRootProcessInstanceId(processInstanceId));
    }

    @Override
    public List<Token> findChildTokensByParentTokenId(Long parentTokenId) {
        return repository.findByParentId(parentTokenId);
    }

    @Override
    public List<Token> findChildTokensByProcessInstanceId(Long processInstanceId) {
        return repository.findChildTokensByProcessInstanceId(processInstanceId);
    }

    @Override
    public List<Token> findInactiveTokensByActivityIdAndProcessInstanceId(String activityId,
            Long processInstanceId) {
        return repository.findInactiveTokensByActivityIdAndProcessInstanceId(activityId,
                processInstanceId, false);
    }

    @Override
    public Token createProcessInstanceToken(ProcessDefinitionEntity processDefinition) {
        Token token = create();
        token.setProcessDefinition(processDefinition);
        token.setScope(true);

        token = save(token);

        token.setProcessInstanceId(token.getId());
        token.setRootProcessInstanceId(token.getId());

        getEventDispatcher().dispatchEvent(
                WorkflowEventFactory.createEntityEvent(WorkflowEventType.ENTITY_CREATED, token));

        return token;
    }

    @Override
    public Token createChildToken(Token parentToken) {

        Token childToken = create();
        childToken = save(childToken);

        childToken.setParent(parentToken);
        childToken.setProcessDefinition(parentToken.getProcessDefinition());
        if (parentToken.getProcessInstanceId() != null) {
            childToken.setProcessInstanceId(parentToken.getProcessInstanceId());
        }
        childToken.setRootProcessInstanceId(parentToken.getRootProcessInstanceId());
        childToken.setScope(false);

        parentToken.addChildToken(childToken);

        getEventDispatcher().dispatchEvent(WorkflowEventFactory
                .createEntityEvent(WorkflowEventType.ENTITY_CREATED, childToken));
        getEventDispatcher().dispatchEvent(WorkflowEventFactory
                .createEntityEvent(WorkflowEventType.ENTITY_INITIALIZED, childToken));

        return childToken;
    }

    @Override
    public Token create() {
        return new Token();
    }

    @Override
    public Token save(Token token) {
        return repository.save(token);
    }

    @Override
    public void updateProcessInstanceLockTime(Long processInstanceId) {
        final Date expiry = new Date();
        final int lockMillis = Context.getProcessEngineService().getAsyncExecutor().getJobLockTimeMillis();

        final Calendar c = Calendar.getInstance();
        c.setTime(expiry);
        c.add(Calendar.SECOND, lockMillis);

        final Token token = repository.findOne(processInstanceId);
        token.setLockTime(c.getTime());
        save(token);

    }

    @Override
    public void clearProcessInstanceLockTime(Long processInstanceId) {
        final Token token = repository.findOne(processInstanceId);
        if (token == null) {
            throw new EntityNotFoundException("No token found for id " + processInstanceId);
        }
        token.setLockTime(null);
        save(token);
    }

    @Override
    public void deleteProcessInstanceToken(Long tokenId, String currentFlowElementId,
            String reason) {
        final Token processInstanceToken = findById(tokenId);
        if (processInstanceToken == null) {
            throw new EntityNotFoundException("No process instance found for id " + tokenId);
        }

        if (processInstanceToken.isDeleted()) {
            return;
        }

        for(final Token subToken : processInstanceToken.getChildTokens()) {
            if(subToken.getSubProcessInstance() != null) {
                deleteProcessInstanceCascade(subToken.getSubProcessInstance(), reason);
            }
        }

        for (final Token each : processInstanceToken.getChildTokens()) {
            if (each.isEventScope()) {
                deleteTokenAndRelatedData(each, null);
            }
        }
        deleteChildTokens(processInstanceToken, reason);
        deleteTokenAndRelatedData(processInstanceToken, reason);

        getEventDispatcher().dispatchEvent(WorkflowEventFactory
                .createEntityEvent(WorkflowEventType.PROCESS_COMPLETED, processInstanceToken));

        processInstanceToken.setDeleted(true);
    }

    private void deleteProcessInstanceCascade(Token token, String reason) {
        for(final Token subToken : token.getChildTokens()) {
            if(subToken.getSubProcessInstance() != null) {
                deleteProcessInstanceCascade(subToken, reason);
            }
        }

        // TODO implement deleteProcessInstanceCascade
    }

    @Override
    public void deleteChildTokens(Token token, String reason) {
        final TokenTree tokenTree = findTokenTree(token.getRootProcessInstanceId());
        final TokenTreeNode tokenTreeNode = tokenTree.getNode(token.getId());
        if (tokenTreeNode == null) {
            return;
        }

        final Iterator<TokenTreeNode> iter = tokenTreeNode.iterator();
        while (iter.hasNext()) {
            final Token childToken = iter.next().getToken();
            if (childToken.isActive() && !childToken.isEnded()
                    && !tokenTreeNode.getToken().getId().equals(childToken.getId())) {
                deleteTokenAndRelatedData(childToken, reason);
            }
        }
    }

    @Override
    public void deleteTokenAndRelatedData(Token token, String reason) {
        deleteDataRelatedToToken(token, reason);
        delete(token);
    }

    @Override
    public void deleteDataRelatedToToken(Token token, String reason) {
        token.setEnded(true);
        token.setActive(false);

        // Delete variables related to the token
        final VariableService variableService = Context.getCommandContext().getVariableService();
        for (final String key : token.getVariables().keySet()) {
            token.getVariables().remove(key);
            final VariableEntity variable = variableService.findByName(key);
            variableService.delete(variable);
        }

        // Delete jobs
        final JobService jobService = Context.getCommandContext().getJobService();
        final List<JobEntity> jobs = jobService.findByTokenId(token.getId());
        if (CollectionUtil.isNotEmpty(jobs)) {
            for (final JobEntity job : jobs) {
                token.getJobs().remove(job);
                jobService.delete(job);
                getEventDispatcher().dispatchEvent(WorkflowEventFactory
                        .createEntityEvent(WorkflowEventType.JOB_CANCELLED, job));
            }
        }

        // Delete event subscriptions
        final EventSubscriptionService eventSubscriptionService = Context.getCommandContext()
                .getEventSubscriptionService();
        for (final EventSubscription subscription : token.getEventSubscriptions()) {
            token.getEventSubscriptions().remove(subscription);
            eventSubscriptionService.delete(subscription);
        }
    }

    @Override
    public void delete(Token token) {
        repository.delete(token);
        token.setDeleted(true);

        Context.getCommandContext().getEventDispatcher().dispatchEvent(
                WorkflowEventFactory.createEntityEvent(WorkflowEventType.ENTITY_DELETED, token));
    }

    private WorkflowEventDispatcher getEventDispatcher() {
        return Context.getCommandContext().getEventDispatcher();
    }

}
