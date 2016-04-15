package com.kaaterskil.workflow.engine.behavior;

import org.springframework.beans.BeansException;

import com.kaaterskil.workflow.bpm.common.FlowNode;
import com.kaaterskil.workflow.engine.delegate.ActivityBehavior;
import com.kaaterskil.workflow.engine.exception.WorkflowException;
import com.kaaterskil.workflow.engine.util.ApplicationContextUtil;

public class BehaviorUtil {

    public static ActivityBehavior getBehaviorBean(FlowNode flowNode) {
        if (flowNode == null || flowNode.getBehavior() == null) {
            return null;
        }
        final String behaviorBeanName = flowNode.getBehavior();

        try {
            final ActivityBehavior behavior = (ActivityBehavior) ApplicationContextUtil
                    .getBean(behaviorBeanName);
            return behavior;
        } catch (final BeansException e) {
            throw new WorkflowException("Could not instantiate bean with name " + behaviorBeanName);
        }
    }
}
