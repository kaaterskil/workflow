package com.kaaterskil.workflow.engine.operation;

import com.kaaterskil.workflow.bpm.common.Expression;
import com.kaaterskil.workflow.bpm.common.SequenceFlow;
import com.kaaterskil.workflow.engine.delegate.DelegateToken;
import com.kaaterskil.workflow.engine.exception.WorkflowException;
import com.kaaterskil.workflow.engine.util.ApplicationContextUtil;
import com.kaaterskil.workflow.util.ValidationUtils;

public class ConditionUtil {

    public static boolean isTrue(SequenceFlow sequenceFlow, DelegateToken token) {
        final Expression expression = sequenceFlow.getConditionExpression();
        if (expression != null) {
            final String className = expression.getDocumentation().get(0).getText();
            if (!ValidationUtils.isEmptyOrWhitespace(className)) {
                try {
                    final Condition condition = ApplicationContextUtil.instantiate(className,
                            Condition.class);
                    if (condition.evaluate(token)) {
                        return true;
                    }
                    return false;

                } catch (final Exception e) {
                    throw new WorkflowException("Could not instantiate Condition " + className);
                }
            }
        }
        return true;
    }
}
