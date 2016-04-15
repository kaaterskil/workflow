package com.kaaterskil.workflow.engine.operation;

import org.springframework.beans.BeansException;

import com.kaaterskil.workflow.bpm.common.SequenceFlow;
import com.kaaterskil.workflow.engine.delegate.DelegateToken;
import com.kaaterskil.workflow.engine.exception.WorkflowException;
import com.kaaterskil.workflow.engine.util.ApplicationContextUtil;

public class ConditionUtil {

    public static boolean isTrue(SequenceFlow sequenceFlow, DelegateToken token) {
        final String expression = sequenceFlow.getConditionExpression().getDocumentation().get(0)
                .getText();
        if (expression != null) {
            try {
                final Condition condition = (Condition) ApplicationContextUtil.getBean(expression);

                if (condition.evaluate(token)) {
                    return true;
                }
                return false;

            } catch (final BeansException e) {
                throw new WorkflowException(
                        "Could not instantiate Condition bean with name " + expression);
            }
        }
        return true;
    }
}
