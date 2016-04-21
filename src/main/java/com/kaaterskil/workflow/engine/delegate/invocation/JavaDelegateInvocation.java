package com.kaaterskil.workflow.engine.delegate.invocation;

import com.kaaterskil.workflow.engine.delegate.DelegateToken;
import com.kaaterskil.workflow.engine.delegate.JavaDelegate;

public class JavaDelegateInvocation extends DelegateInvocation {

    private final JavaDelegate delegate;
    private final DelegateToken token;

    public JavaDelegateInvocation(JavaDelegate delegate, DelegateToken token) {
        this.delegate = delegate;
        this.token = token;
    }

    @Override
    public Object getTarget() {
        return delegate;
    }

    @Override
    protected void invoke() {
        delegate.execute(token);
    }

}
