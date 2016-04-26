package com.kaaterskil.workflow.engine.operation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kaaterskil.workflow.bpm.common.FlowElement;
import com.kaaterskil.workflow.bpm.common.FlowNode;
import com.kaaterskil.workflow.engine.behavior.BehaviorHelper;
import com.kaaterskil.workflow.engine.context.Context;
import com.kaaterskil.workflow.engine.delegate.ActivityBehavior;
import com.kaaterskil.workflow.engine.delegate.TokenListener;
import com.kaaterskil.workflow.engine.delegate.event.WorkflowEventFactory;
import com.kaaterskil.workflow.engine.delegate.event.WorkflowEventType;
import com.kaaterskil.workflow.engine.exception.WorkflowException;
import com.kaaterskil.workflow.engine.interceptor.CommandContext;
import com.kaaterskil.workflow.engine.persistence.entity.Token;
import com.kaaterskil.workflow.util.CollectionUtil;

public class ContinueMultiInstanceOperation extends AbstractOperation {
    private static final Logger log = LoggerFactory.getLogger(ContinueMultiInstanceOperation.class);

    public ContinueMultiInstanceOperation(CommandContext commandContext, Token token) {
        super(commandContext, token);
    }

    @Override
    public void run() {
        FlowElement currentFlowElement = token.getCurrentFlowElement();
        if (currentFlowElement == null) {
            currentFlowElement = findCurrentFlowElement(token);
            token.setCurrentFlowElement(currentFlowElement);
        }

        if (currentFlowElement instanceof FlowNode) {
            continueThroughFlowNode((FlowNode) currentFlowElement);
        } else {
            throw new WorkflowException("No valid multi-instance flow node. Halting.");
        }

    }
    private void continueThroughFlowNode(FlowNode flowNode) {
        if (CollectionUtil.isNotEmpty(flowNode.getTokenListeners())) {
            executeTokenListeners(flowNode, TokenListener.EVENT_START);
        }

        final BehaviorHelper behaviorHelper = commandContext.getProcessEngineService()
                .getBehaviorHelper(token);
        final ActivityBehavior behavior = behaviorHelper.getBehavior(flowNode);
        if (behavior != null) {
            log.debug("Executing activityBehavior {} on activity {} with token {}",
                    behavior.getClass().getSimpleName(), flowNode.getId(), token.getId());

            Context.getProcessEngineService().getEventDispatcher()
                    .dispatchEvent(WorkflowEventFactory.createActivityEvent(
                            WorkflowEventType.ACTIVITY_STARTED, flowNode.getId(),
                            flowNode.getName(), token, flowNode));

            behavior.execute(token);

        } else {
            log.debug("No activityBehavior on activity {} with token {}", flowNode.getId(),
                    token.getId());
        }
    }

}
