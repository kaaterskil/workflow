package com.kaaterskil.workflow.engine.delegate;

import com.kaaterskil.workflow.engine.delegate.invocation.DelegateInvocation;

public abstract class ActivityBehaviorInvocation extends DelegateInvocation {

    private final ActivityBehavior behaviorInstance;
    private final DelegateToken token;

    public ActivityBehaviorInvocation(ActivityBehavior behaviorInstance, DelegateToken token) {
        this.behaviorInstance = behaviorInstance;
        this.token = token;
    }

    @Override
    public Object getTarget() {
        return behaviorInstance;
    }

    @Override
    protected void invoke() {
        behaviorInstance.execute(token);
    }

}
