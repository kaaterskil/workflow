package com.kaaterskil.workflow.engine.operation;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kaaterskil.workflow.bpm.common.FlowElement;
import com.kaaterskil.workflow.bpm.common.FlowNode;
import com.kaaterskil.workflow.bpm.common.SequenceFlow;
import com.kaaterskil.workflow.bpm.common.activity.Activity;
import com.kaaterskil.workflow.bpm.common.activity.EventSubProcess;
import com.kaaterskil.workflow.bpm.common.event.BoundaryEvent;
import com.kaaterskil.workflow.bpm.common.event.CancelEventDefinition;
import com.kaaterskil.workflow.bpm.common.gateway.ComplexGateway;
import com.kaaterskil.workflow.bpm.common.gateway.ExclusiveGateway;
import com.kaaterskil.workflow.bpm.common.gateway.InclusiveGateway;
import com.kaaterskil.workflow.bpm.common.process.Process;
import com.kaaterskil.workflow.engine.context.Context;
import com.kaaterskil.workflow.engine.delegate.TokenListener;
import com.kaaterskil.workflow.engine.delegate.event.WorkflowEventFactory;
import com.kaaterskil.workflow.engine.delegate.event.WorkflowEventType;
import com.kaaterskil.workflow.engine.exception.WorkflowException;
import com.kaaterskil.workflow.engine.interceptor.CommandContext;
import com.kaaterskil.workflow.engine.persistence.entity.Token;
import com.kaaterskil.workflow.engine.service.TokenService;
import com.kaaterskil.workflow.engine.util.ProcessDefinitionUtil;

public class OutgoingSequenceFlowOperation extends AbstractOperation {
    private static final Logger log = LoggerFactory.getLogger(OutgoingSequenceFlowOperation.class);

    protected boolean evaluateConditions;

    public OutgoingSequenceFlowOperation(CommandContext commandContext, Token token,
            boolean evaluateConditions) {
        super(commandContext, token);
        this.evaluateConditions = evaluateConditions;
    }

    @Override
    public void run() {
        FlowElement currentFlowElement = token.getCurrentFlowElement();
        if (currentFlowElement == null) {
            currentFlowElement = findCurrentFlowElement(token);
            token.setCurrentFlowElement(currentFlowElement);
        }

        if (token.getParentId() != null && token.isScope()) {
            workflow.addDestroyScopeOperation(token);

        } else if (currentFlowElement instanceof Activity) {
            final Activity activity = (Activity) currentFlowElement;
            if (activity != null && !activity.getBoundaryEventRefs().isEmpty()) {
                final List<String> eventRefsToKeep = new ArrayList<>();

                for (final String eventRef : activity.getBoundaryEventRefs()) {
                    final Process process = ProcessDefinitionUtil
                            .getProcess(token.getProcessDefinitionId());
                    final BoundaryEvent event = (BoundaryEvent) process.getFlowElement(eventRef,
                            true);
                    if (event.getEventDefinitions() != null
                            && !event.getEventDefinitions().isEmpty() && event.getEventDefinitions()
                                    .get(0) instanceof CancelEventDefinition) {
                        eventRefsToKeep.add(eventRef);
                    }
                }

                final List<Token> childTokens = commandContext.getTokenService()
                        .findChildTokensByParentTokenId(token.getId());
                for (final Token childToken : childTokens) {
                    if (childToken.getCurrentFlowElement() == null || !eventRefsToKeep
                            .contains(childToken.getCurrentFlowElement().getId())) {
                        commandContext.getTokenService().deleteTokenAndRelatedData(childToken,
                                null);
                    }
                }
            }
        }

        if (!currentFlowElement.getTokenListeners().isEmpty() && !token.isProcessInstanceType()) {
            executeTokenListeners(currentFlowElement, TokenListener.EVENT_END);
        }

        if (currentFlowElement instanceof FlowNode) {
            final FlowNode flowNode = (FlowNode) currentFlowElement;
            if (!(token.getId().equals(token.getProcessInstanceId()))) {
                Context.getProcessEngineService().getEventDispatcher()
                        .dispatchEvent(WorkflowEventFactory.createActivityEvent(
                                WorkflowEventType.ACTIVITY_COMPLETED, flowNode.getId(),
                                flowNode.getName(), token, flowNode));
            }

            leaveFlowNode(flowNode);

        } else if (currentFlowElement instanceof SequenceFlow) {
            workflow.addContinueProcessOperation(token);
        }
    }

    private void leaveFlowNode(FlowNode flowNode) {
        log.debug("Leaving flow node {} with id {} by following its {} outgoing sequence flow",
                flowNode.getClass(), flowNode.getId(), flowNode.getOutgoing().size());

        // Get the default sequence flow
        String defaultSequenceFlowId = null;
        if (flowNode instanceof Activity) {
            defaultSequenceFlowId = ((Activity) flowNode).getDefaultSequenceFlow();
        } else if (flowNode instanceof ComplexGateway) {
            defaultSequenceFlowId = ((ComplexGateway) flowNode).getDefaultSequenceFlow();
        } else if (flowNode instanceof InclusiveGateway) {
            defaultSequenceFlowId = ((InclusiveGateway) flowNode).getDefaultSequenceFlow();
        } else if (flowNode instanceof ExclusiveGateway) {
            defaultSequenceFlowId = ((ExclusiveGateway) flowNode).getDefaultSequenceFlow();
        }

        // Test condition
        final List<SequenceFlow> outgoingSequenceFlow = new ArrayList<>();
        for (final SequenceFlow sequenceFlow : flowNode.getOutgoing()) {
            if (!evaluateConditions
                    || (ConditionUtil.isTrue(sequenceFlow, token) && (defaultSequenceFlowId == null
                            || !defaultSequenceFlowId.equals(sequenceFlow.getId())))) {
                outgoingSequenceFlow.add(sequenceFlow);
            }
        }

        // Do we have a default sequence flow?
        if (outgoingSequenceFlow.isEmpty() && evaluateConditions) {
            if (defaultSequenceFlowId != null) {
                for (final SequenceFlow sequenceFlow : flowNode.getOutgoing()) {
                    if (defaultSequenceFlowId.equals(sequenceFlow.getId())) {
                        outgoingSequenceFlow.add(sequenceFlow);
                        break;
                    }
                }
            }
        }

        // Still no outgoing flow? End the token.
        if (outgoingSequenceFlow.isEmpty()) {
            if (flowNode.getOutgoing().isEmpty()) {
                log.debug("No outgoing sequence flow for flow node {}", flowNode.getId());

                if (flowNode.getSubProcess() != null
                        && flowNode.getSubProcess() instanceof EventSubProcess) {
                    final TokenService tokenService = Context.getCommandContext().getTokenService();
                    tokenService.deleteChildTokens(token, null);
                    tokenService.deleteTokenAndRelatedData(token, null);

                    if (flowNode.getSubProcess().getSubProcess() != null) {
                        tokenService.deleteChildTokens(token.getParent(), null);
                        tokenService.deleteTokenAndRelatedData(token.getParent(), null);

                        final Token parentToken = token.getParent().getParent();
                        parentToken.setCurrentFlowElement(flowNode.getSubProcess().getSubProcess());
                        workflow.addOutgoingSequenceFlow(parentToken, true);
                    } else {
                        tokenService.deleteChildTokens(token.getParent(), null);
                        workflow.addEndTokenOperation(token.getParent());
                    }
                } else {
                    workflow.addEndTokenOperation(token);
                }
                return;

            } else {
                throw new WorkflowException("No outgoing sequence flow of element "
                        + flowNode.getId() + " could be selected for continuing the process.");
            }
        }

        // Now leave
        final TokenService tokenService = commandContext.getTokenService();
        final List<Token> outgoingTokens = new ArrayList<>();

        SequenceFlow sequenceFlow = outgoingSequenceFlow.get(0);
        token.setCurrentFlowElement(sequenceFlow);
        token.setActive(true);
        outgoingTokens.add(token);

        if (outgoingSequenceFlow.size() > 1) {
            for (int i = 1; i < outgoingSequenceFlow.size(); i++) {
                final Token outgoingToken = tokenService.create();
                outgoingToken.setProcessDefinition(token.getProcessDefinition());
                outgoingToken.setProcessInstanceId(token.getProcessInstanceId());
                outgoingToken.setRootProcessInstanceId(token.getRootProcessInstanceId());
                outgoingToken.setScope(false);
                outgoingToken.setActive(true);
                outgoingToken.setParent(token.getParent() != null ? token.getParent() : token);

                sequenceFlow = outgoingSequenceFlow.get(i);
                outgoingToken.setCurrentFlowElement(sequenceFlow);

                final Token savedOutgoingToken = tokenService.save(outgoingToken);
                outgoingTokens.add(savedOutgoingToken);
            }
        }

        for (final Token outgoingToken : outgoingTokens) {
            workflow.addContinueProcessOperation(outgoingToken);
        }
    }

}
