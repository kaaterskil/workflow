package com.kaaterskil.workflow.engine.delegate.event;

import org.springframework.stereotype.Component;

import com.kaaterskil.workflow.bpm.foundation.BaseElement;
import com.kaaterskil.workflow.engine.delegate.DelegateToken;
import com.kaaterskil.workflow.engine.delegate.TokenListener;

@Component
public class MockTokenListener extends BaseElement implements TokenListener {

    private String message;

    @Override
    public void notify(DelegateToken token) {
        setMessage("Token listener called for token " + token.getId());
        System.out.println(message);
    }

    @Override
    public String toString() {
        return String.format("MockTokenListener [message=%s]", message);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
