package com.kaaterskil.workflow.engine.behavior;

import com.kaaterskil.workflow.engine.delegate.DelegateToken;
import com.kaaterskil.workflow.engine.delegate.TriggerableActivityBehavior;
import com.kaaterskil.workflow.engine.exception.WorkflowException;
import com.kaaterskil.workflow.engine.persistence.entity.Token;

public abstract class FlowNodeActivityBehavior implements TriggerableActivityBehavior {

    protected BehaviorHelper helper = new BehaviorHelper();

    /**
     * Default behavior - just leave the activity with no extra functionality.
     */
    @Override
    public void execute(DelegateToken token) {
        leave(token);
    }

    public void leave(DelegateToken token){
        helper.performOutgoingBehavior((Token) token);
    }

    public void leaveIgnoreConditions(DelegateToken token) {
        helper.performOutgoingBehaviorIgnoreConditions((Token) token);
    }

    @Override
    public void trigger(DelegateToken token, String signalEvent, Object signalData) {
        throw new WorkflowException("This activity is not waiting for a trigger");
    }

}
