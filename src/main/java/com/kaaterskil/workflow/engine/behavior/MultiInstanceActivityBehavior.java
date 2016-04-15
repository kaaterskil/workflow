package com.kaaterskil.workflow.engine.behavior;

import com.kaaterskil.workflow.bpm.common.Expression;
import com.kaaterskil.workflow.bpm.common.activity.Activity;
import com.kaaterskil.workflow.engine.delegate.DelegateToken;
import com.kaaterskil.workflow.engine.delegate.SubProcessActivityBehavior;
import com.kaaterskil.workflow.engine.exception.WorkflowException;

public abstract class MultiInstanceActivityBehavior extends FlowNodeActivityBehavior
        implements SubProcessActivityBehavior {

    private Activity activity;
    private BaseActivityBehavior innerActivityBehavior;
    private Expression loopCardinalityExpression;
    private Expression completionConditionExpression;

    public MultiInstanceActivityBehavior(Activity activity,
            BaseActivityBehavior innerActivityBehavior) {
        this.activity = activity;
        this.innerActivityBehavior = innerActivityBehavior;
    }

    @Override
    public void execute(DelegateToken token) {
        if (getLoopVariable(token, "loopCounter") == null) {
            int numInstances = 0;

            try {
                numInstances = createInstances(token);
            } catch (final Exception e) {
                throw new WorkflowException("Error creating instances for MultiInstanceBehavior");
            }

            if (numInstances == 0) {
                super.leave(token);
            }
        } else {
            innerActivityBehavior.execute(token);
        }
    }

    @Override
    public void completing(DelegateToken token, DelegateToken subProcessToken) throws Exception {
        // Noop
    }

    @Override
    public void completed(DelegateToken token) throws Exception {
        leave(token);
    }

    protected abstract int createInstances(DelegateToken token);

    protected Integer getLoopVariable(DelegateToken token, String variableName) {
        return (Integer) token.getVariable(variableName);
    }

    /*---------- Getter/Setters ----------*/

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public BaseActivityBehavior getInnerActivityBehavior() {
        return innerActivityBehavior;
    }

    public void setInnerActivityBehavior(BaseActivityBehavior innerActivityBehavior) {
        this.innerActivityBehavior = innerActivityBehavior;
    }

    public Expression getLoopCardinalityExpression() {
        return loopCardinalityExpression;
    }

    public void setLoopCardinalityExpression(Expression loopCardinalityExpression) {
        this.loopCardinalityExpression = loopCardinalityExpression;
    }

    public Expression getCompletionConditionExpression() {
        return completionConditionExpression;
    }

    public void setCompletionConditionExpression(Expression completionConditionExpression) {
        this.completionConditionExpression = completionConditionExpression;
    }
}
