package com.kaaterskil.workflow.engine.behavior;

import com.kaaterskil.workflow.engine.context.Context;
import com.kaaterskil.workflow.engine.delegate.ActivityBehavior;
import com.kaaterskil.workflow.engine.delegate.DelegateToken;
import com.kaaterskil.workflow.engine.delegate.JavaDelegate;
import com.kaaterskil.workflow.engine.delegate.TokenListener;
import com.kaaterskil.workflow.engine.delegate.invocation.JavaDelegateInvocation;

public class ServiceTaskJavaDelegateActivityBehavior extends TaskActivityBehavior
        implements ActivityBehavior, TokenListener {

    private final JavaDelegate javaDelegate;

    public ServiceTaskJavaDelegateActivityBehavior(JavaDelegate javaDelegate) {
        this.javaDelegate = javaDelegate;
    }

    @Override
    public void execute(DelegateToken token) {
        Context.getProcessEngineService().getDelegateInterceptor()
                .handleInvocation(new JavaDelegateInvocation(javaDelegate, token));
        leave(token);
    };

    @Override
    public void notify(DelegateToken token) {
        execute(token);
    }

}
