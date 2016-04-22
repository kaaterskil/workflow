package com.kaaterskil.workflow.engine.event;

import java.util.List;
import java.util.Map;

import com.kaaterskil.workflow.bpm.common.FlowElement;
import com.kaaterskil.workflow.bpm.common.FlowNode;
import com.kaaterskil.workflow.bpm.common.activity.SubProcess;
import com.kaaterskil.workflow.bpm.common.event.BoundaryEvent;
import com.kaaterskil.workflow.bpm.common.process.Process;
import com.kaaterskil.workflow.engine.context.Context;
import com.kaaterskil.workflow.engine.delegate.event.WorkflowEventFactory;
import com.kaaterskil.workflow.engine.exception.WorkflowException;
import com.kaaterskil.workflow.engine.interceptor.CommandContext;
import com.kaaterskil.workflow.engine.persistence.entity.EventSubscription;
import com.kaaterskil.workflow.engine.persistence.entity.Token;
import com.kaaterskil.workflow.engine.util.ProcessDefinitionUtil;
import com.kaaterskil.workflow.util.CollectionUtil;

public abstract class AbstractEventHandler implements EventHandler {

    @Override
    public void handleEvent(EventSubscription eventSubscription, Object payload,
            CommandContext commandContext) {
        final Token token = eventSubscription.getToken();
        final FlowNode currentFlowElement = (FlowNode) token.getCurrentFlowElement();
        if (currentFlowElement == null) {
            throw new WorkflowException(
                    "Error while handling event subscrtiption " + eventSubscription.getId());
        }

        if (payload instanceof Map) {
            @SuppressWarnings("unchecked")
            final Map<String, Object> variables = (Map<String, Object>) payload;
            token.setVariables(variables);
        }

        if (currentFlowElement instanceof BoundaryEvent) {
            dispatchActivityCancelledEventsIfNeeded(eventSubscription, token, currentFlowElement);
        }

        Context.getCommandContext().getWorkflow().addTriggerTokenOperation(token);
    }

    private void dispatchActivityCancelledEventsIfNeeded(EventSubscription eventSubscription,
            Token token, FlowElement currentFlowElement) {
        if (currentFlowElement instanceof BoundaryEvent) {
            final BoundaryEvent boundaryEvent = (BoundaryEvent) currentFlowElement;
            if (boundaryEvent.isCancelActivity()) {
                dispatchTokenCancelledEvent(eventSubscription, token);
            }
        }
    }

    private void dispatchTokenCancelledEvent(EventSubscription eventSubscription, Token token) {
        // subprocesses
        for (final Token subToken : token.getChildTokens()) {
            dispatchTokenCancelledEvent(eventSubscription, subToken);
        }

        // call activities

        // Activity with message/signal boundary events
        final FlowElement flowElement = token.getCurrentFlowElement();
        if (flowElement != null && flowElement instanceof BoundaryEvent) {
            final BoundaryEvent boundaryEvent = (BoundaryEvent) flowElement;
            if (boundaryEvent.getAttachedToRef() != null) {
                final Process process = ProcessDefinitionUtil
                        .getProcess(token.getProcessDefinitionId());
                final FlowNode flowNode = (FlowNode) process
                        .getFlowElement(boundaryEvent.getAttachedToRef(), true);
                dispatchActivityCancelledEvent(eventSubscription, token, flowNode);
            }
        }
    }

    private void dispatchActivityCancelledEvent(EventSubscription eventSubscription,
            Token boundaryEventToken, FlowNode flowNode) {

        Context.getProcessEngineService().getEventDispatcher()
                .dispatchEvent(WorkflowEventFactory.createActivityCancelledEvent(flowNode.getId(),
                        flowNode.getName(), boundaryEventToken.getId(),
                        boundaryEventToken.getProcessInstanceId(),
                        boundaryEventToken.getProcessDefinitionId(), flowNode.getClass().getName(),
                        flowNode.getBehavior(), eventSubscription));

        if (flowNode instanceof SubProcess) {
            final Token parentToken = Context.getCommandContext().getTokenService()
                    .findById(boundaryEventToken.getParentId());
            if (parentToken != null) {
                dispatchAcvitityCancelledEventForChildToken(eventSubscription, parentToken,
                        boundaryEventToken);
            }
        }
    }

    private void dispatchAcvitityCancelledEventForChildToken(EventSubscription eventSubscription,
            Token parentToken, Token boundaryEventToken) {

        final List<Token> tokens = Context.getCommandContext().getTokenService()
                .findChildTokensByParentTokenId(parentToken.getId());
        if (CollectionUtil.isNotEmpty(tokens)) {
            for (final Token childToken : tokens) {
                if (!boundaryEventToken.getId().equals(childToken.getId())
                        && childToken.getCurrentFlowElement() != null
                        && childToken.getCurrentFlowElement() instanceof FlowNode) {
                    final FlowNode flowNode = (FlowNode) childToken.getCurrentFlowElement();
                    Context.getProcessEngineService().getEventDispatcher()
                            .dispatchEvent(WorkflowEventFactory.createActivityCancelledEvent(
                                    flowNode.getId(), flowNode.getName(), childToken.getId(),
                                    childToken.getProcessInstanceId(),
                                    childToken.getProcessDefinitionId(),
                                    flowNode.getClass().getName(), flowNode.getBehavior(),
                                    eventSubscription));
                }
            }
        }
    }
}
