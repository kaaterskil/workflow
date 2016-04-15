package com.kaaterskil.workflow.engine.interceptor;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import com.kaaterskil.workflow.engine.ProcessEngineService;
import com.kaaterskil.workflow.engine.RepositoryService;
import com.kaaterskil.workflow.engine.delegate.event.WorkflowEventDispatcher;
import com.kaaterskil.workflow.engine.operation.Workflow;
import com.kaaterskil.workflow.engine.persistence.entity.Token;
import com.kaaterskil.workflow.engine.service.ActivityService;
import com.kaaterskil.workflow.engine.service.DeploymentService;
import com.kaaterskil.workflow.engine.service.EventSubscriptionService;
import com.kaaterskil.workflow.engine.service.JobService;
import com.kaaterskil.workflow.engine.service.ProcessDefinitionService;
import com.kaaterskil.workflow.engine.service.ProcessInstanceService;
import com.kaaterskil.workflow.engine.service.TokenService;
import com.kaaterskil.workflow.engine.service.VariableService;

public class CommandContext {

    private final ProcessEngineService processEngineService;
    private final RepositoryService repositoryService;

    private final Workflow workflow = new Workflow(this);
    private final Map<Serializable, Token> tokens = new HashMap<>();
    private final LinkedList<Object> resultStack = new LinkedList<>();

    public CommandContext(ProcessEngineService processEngineService) {
        this.processEngineService = processEngineService;
        this.repositoryService = processEngineService.getRepositoryService();
    }

    public ProcessEngineService getProcessEngineService() {
        return processEngineService;
    }

    public DeploymentService getDeploymentService() {
        return repositoryService.getDeploymentService();
    }

    public ProcessDefinitionService getProcessDefinitionService() {
        return repositoryService.getProcessDefinitionService();
    }

    public ProcessInstanceService getProcessInstanceService() {
        return repositoryService.getProcessInstanceService();
    }

    public ActivityService getActivityService() {
        return repositoryService.getActivityService();
    }

    public TokenService getTokenService() {
        return repositoryService.getTokenService();
    }

    public VariableService getVariableService() {
        return repositoryService.getVariableService();
    }

    public JobService getJobService() {
        return repositoryService.getJobService();
    }

    public WorkflowEventDispatcher getEventDispatcher() {
        return processEngineService.getEventDispatcher();
    }

    public EventSubscriptionService getEventSubscriptionService() {
        return repositoryService.getEventSubscriptionService();
    }

    /*---------- Tokens ----------*/

    public void addToken(Token token) {
        if (token.getId() != null) {
            tokens.put(token.getId(), token);
        }
    }

    public boolean hasTokens() {
        return tokens.size() > 0;
    }

    public Collection<Token> getTokens() {
        return tokens.values();
    }

    /*---------- Getter/Setters ----------*/

    public Workflow getWorkflow() {
        return workflow;
    }

    public Object getResult() {
        return resultStack.pollLast();
    }

    public void setResult(Object result) {
        resultStack.add(result);
    }
}
