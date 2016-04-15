package com.kaaterskil.workflow.engine.delegate.invocation;

import com.kaaterskil.workflow.engine.delegate.DelegateToken;
import com.kaaterskil.workflow.engine.delegate.TokenListener;

public class TokenListenerInvocation extends DelegateInvocation {

    private final TokenListener tokenListenerInstance;
    private final DelegateToken token;

    public TokenListenerInvocation(TokenListener tokenListenerInstance, DelegateToken token) {
        this.tokenListenerInstance = tokenListenerInstance;
        this.token = token;
    }

    @Override
    public Object getTarget() {
        return tokenListenerInstance;
    }

    @Override
    protected void invoke() {
        tokenListenerInstance.notify(token);
    }

}
