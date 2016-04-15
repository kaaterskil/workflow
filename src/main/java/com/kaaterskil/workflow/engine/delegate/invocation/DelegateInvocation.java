package com.kaaterskil.workflow.engine.delegate.invocation;

public abstract class DelegateInvocation {

    protected Object invocationResult;

    public void proceed() {
        invoke();
    }

    public abstract Object getTarget();

    protected abstract void invoke();

    /*---------- Getter/Setters ----------*/

    public Object getInvocationResult() {
        return invocationResult;
    }

    public void setInvocationResult(Object invocationResult) {
        this.invocationResult = invocationResult;
    }
}
