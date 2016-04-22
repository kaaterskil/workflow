package com.kaaterskil.workflow.engine.operation;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kaaterskil.workflow.bpm.common.FlowElement;
import com.kaaterskil.workflow.bpm.common.activity.Activity;
import com.kaaterskil.workflow.bpm.common.activity.SubProcess;
import com.kaaterskil.workflow.bpm.common.activity.Transaction;
import com.kaaterskil.workflow.bpm.common.event.BoundaryEvent;
import com.kaaterskil.workflow.bpm.common.event.CompensateEventDefinition;
import com.kaaterskil.workflow.bpm.common.event.EndEvent;
import com.kaaterskil.workflow.bpm.common.process.Process;
import com.kaaterskil.workflow.engine.behavior.MultiInstanceActivityBehavior;
import com.kaaterskil.workflow.engine.delegate.ActivityBehavior;
import com.kaaterskil.workflow.engine.delegate.TokenListener;
import com.kaaterskil.workflow.engine.interceptor.CommandContext;
import com.kaaterskil.workflow.engine.persistence.entity.Token;
import com.kaaterskil.workflow.engine.service.TokenService;
import com.kaaterskil.workflow.engine.util.ApplicationContextUtil;
import com.kaaterskil.workflow.engine.util.ProcessDefinitionUtil;
import com.kaaterskil.workflow.engine.util.ScopeUtil;
import com.kaaterskil.workflow.util.CollectionUtil;

public class EndTokenOperation extends AbstractOperation {
    private static final Logger log = LoggerFactory.getLogger(EndTokenOperation.class);

    public EndTokenOperation(CommandContext commandContext, Token token) {
        super(commandContext, token);
    }

    @Override
    public void run() {
        final TokenService tokenService = commandContext.getTokenService();
        final Token tokenEntity = token;

        Token parentToken = null;
        if (tokenEntity.getParentId() != null) {
            parentToken = tokenService.findById(tokenEntity.getParentId());
        }

        if (parentToken != null) {
            if (tokenEntity.isScope()) {
                tokenService.deleteChildTokens(tokenEntity, null);
            }

            log.debug("Ending token {}", token.getId());
            tokenService.deleteTokenAndRelatedData(parentToken, null);

            log.debug("Parent token found. Continuing process using token {}", parentToken.getId());

            SubProcess subProcess = null;
            if (token.getCurrentFlowElement() instanceof EndEvent) {
                final EndEvent endEvent = (EndEvent) token.getCurrentFlowElement();
                subProcess = endEvent.getSubProcess();

                ActivityBehavior subProcessBehavior = null;
                if (subProcess != null && subProcess.getBehavior() != null) {
                    subProcessBehavior = (ActivityBehavior) ApplicationContextUtil
                            .getBean(subProcess.getBehavior());
                }

                if (!parentToken.getId().equals(parentToken.getProcessInstanceId())
                        && subProcess != null && subProcess.getLoopCharacteristics() != null
                        && (subProcessBehavior != null
                                && subProcessBehavior instanceof MultiInstanceActivityBehavior)) {

                    final List<Token> childTokens = getActiveChildTokensForToken(
                            parentToken.getId());
                    boolean containsOthers = false;
                    for (final Token each : childTokens) {
                        if (!each.getId().equals(tokenEntity.getId())) {
                            containsOthers = true;
                        }
                    }
                    if (!containsOthers) {
                        final MultiInstanceActivityBehavior multiInstanceBehavior = (MultiInstanceActivityBehavior) subProcessBehavior;
                        parentToken.setCurrentFlowElement(subProcess);
                        multiInstanceBehavior.leave(parentToken);
                    }
                    return;
                }
            }

            // Continue the process
            if (getNumberOfActiveChildTokensForToken(parentToken.getId()) == 0
                    || isAllEventScope(parentToken)) {

                if (subProcess != null) {
                    parentToken.setCurrentFlowElement(subProcess);

                    boolean hasCompensation = false;
                    if (subProcess instanceof Transaction) {
                        hasCompensation = true;
                    } else {
                        final Process process = ProcessDefinitionUtil
                                .getProcess(parentToken.getProcessDefinitionId());
                        for (final FlowElement each : subProcess.getFlowElements()) {
                            if (each instanceof Activity) {
                                final Activity subActivity = (Activity) each;
                                if (CollectionUtil.isNotEmpty(subActivity.getBoundaryEventRefs())) {
                                    for (final String boundaryEventRef : subActivity
                                            .getBoundaryEventRefs()) {
                                        final BoundaryEvent event = (BoundaryEvent) process
                                                .getFlowElement(boundaryEventRef, true);
                                        if (CollectionUtil.isNotEmpty(event.getEventDefinitions())
                                                && (event.getEventDefinitions().get(
                                                        0) instanceof CompensateEventDefinition)) {
                                            hasCompensation = true;
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if (hasCompensation) {
                        ScopeUtil.createCopyOfSubProcessTokenForCompensation(parentToken,
                                parentToken.getParent());
                    }
                } else {
                    if (tokenEntity.getCurrentFlowElement() instanceof Activity) {
                        final Activity activity = (Activity) tokenEntity.getCurrentFlowElement();
                        if (activity.isForCompensation()) {
                            return;
                        }
                    }
                    if (!(parentToken.getCurrentFlowElement() instanceof SubProcess)) {
                        parentToken.setCurrentFlowElement(tokenEntity.getCurrentFlowElement());
                    }
                }

                workflow.addOutgoingSequenceFlow(parentToken, true);
            }
        } else {
            final Long processInstanceId = tokenEntity.getId();

            // TODO implement super process

            final int activeTokens = getNumberOfActiveChildTokensForProcessInstance(
                    tokenEntity.getId());
            if (activeTokens == 0) {
                log.debug("No active executions found. Ending process instance {}",
                        processInstanceId);
                final String currentFlowElementId = tokenEntity.getCurrentFlowElement() != null
                        ? tokenEntity.getCurrentFlowElement().getId() : null;
                tokenService.deleteProcessInstanceToken(processInstanceId, currentFlowElementId,
                        "FINISHED");
            } else {
                log.debug("Active executions found. Process instance {} will not be ended",
                        processInstanceId);
            }

            final Process process = getProcess(token.getProcessDefinitionId());
            if (CollectionUtil.isNotEmpty(process.getTokenListeners())) {
                executeTokenListeners(process, token, TokenListener.EVENT_END);
            }
        }
    }

    protected Process getProcess(Long processDefinitionId) {
        return ProcessDefinitionUtil.getProcess(processDefinitionId);
    }

    protected int getNumberOfActiveChildTokensForProcessInstance(Long processInstanceId) {
        final TokenService tokenService = commandContext.getTokenService();
        final List<Token> executions = tokenService
                .findChildTokensByProcessInstanceId(processInstanceId);

        int activeTokens = 0;
        for (final Token each : executions) {
            if (each.isActive() && !processInstanceId.equals(each.getId())) {
                activeTokens++;
            }
        }
        return activeTokens;
    }

    protected List<Token> getActiveChildTokensForToken(Long tokenId) {
        final TokenService tokenService = commandContext.getTokenService();
        final List<Token> childTokens = tokenService.findChildTokensByParentTokenId(tokenId);

        final List<Token> result = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(childTokens)) {
            for (final Token each : childTokens) {
                if (!(each.getCurrentFlowElement() instanceof BoundaryEvent)) {
                    result.add(each);
                }
            }
        }
        return result;
    }

    protected int getNumberOfActiveChildTokensForToken(Long executionId) {
        final TokenService tokenService = commandContext.getTokenService();
        final List<Token> executions = tokenService.findChildTokensByParentTokenId(executionId);

        int activeTokens = 0;
        for (final Token each : executions) {
            if (!(each.getCurrentFlowElement() instanceof BoundaryEvent)) {
                activeTokens++;
            }
        }
        return activeTokens;
    }

    protected boolean isAllEventScope(Token parentToken) {
        final TokenService tokenService = commandContext.getTokenService();

        boolean isAllEventScope = true;
        final List<Token> tokens = tokenService.findChildTokensByParentTokenId(parentToken.getId());
        if (CollectionUtil.isNotEmpty(tokens)) {
            for (final Token each : tokens) {
                if (each.isEventScope()) {
                    tokenService.deleteTokenAndRelatedData(each, null);
                } else {
                    isAllEventScope = false;
                }
            }
        }
        return isAllEventScope;
    }

}
