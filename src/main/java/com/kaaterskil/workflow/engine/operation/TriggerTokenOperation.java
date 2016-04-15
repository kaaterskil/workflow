package com.kaaterskil.workflow.engine.operation;

import org.springframework.beans.BeansException;

import com.kaaterskil.workflow.bpm.common.FlowElement;
import com.kaaterskil.workflow.bpm.common.FlowNode;
import com.kaaterskil.workflow.engine.delegate.ActivityBehavior;
import com.kaaterskil.workflow.engine.delegate.TriggerableActivityBehavior;
import com.kaaterskil.workflow.engine.exception.WorkflowException;
import com.kaaterskil.workflow.engine.interceptor.CommandContext;
import com.kaaterskil.workflow.engine.persistence.entity.Token;
import com.kaaterskil.workflow.engine.util.ApplicationContextUtil;

public class TriggerTokenOperation extends AbstractOperation {

    public TriggerTokenOperation(CommandContext commandContext, Token token) {
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
            final FlowNode flowNode = (FlowNode) currentFlowElement;

            try {
                final ActivityBehavior activityBehavior = (ActivityBehavior) ApplicationContextUtil
                        .getBean(flowNode.getBehavior());
                if (activityBehavior instanceof TriggerableActivityBehavior) {
                    ((TriggerableActivityBehavior) activityBehavior).trigger(token, null, null);
                } else {
                    throw new WorkflowException(
                            "Specified behavior does not implement TriggerableActivityBehavior");
                }
            } catch (final BeansException e) {
                throw new WorkflowException(
                        "Could not instantiate behavior {}" + flowNode.getBehavior());
            }
        } else {
            throw new WorkflowException(
                    "No current flow element found or invalid type: " + currentFlowElement);
        }

    }

}
