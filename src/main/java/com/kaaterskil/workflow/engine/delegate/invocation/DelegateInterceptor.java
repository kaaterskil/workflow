package com.kaaterskil.workflow.engine.delegate.invocation;

public class DelegateInterceptor {

    public void handleInvocation(DelegateInvocation invocation){
        invocation.proceed();
    }
}
