package com.kaaterskil.workflow.engine.delegate.event;

import org.springframework.stereotype.Component;

import com.kaaterskil.workflow.bpm.foundation.BaseElement;
import com.kaaterskil.workflow.engine.delegate.DelegateToken;
import com.kaaterskil.workflow.engine.delegate.TokenListener;

@Component
public class MockTokenListener extends BaseElement implements TokenListener {

    @Override
    public void notify(DelegateToken token) {
        System.out.println("Token listener called with token " + token.getId());
    }

    @Override
    public String toString() {
        return String.format("MockTokenListener []");
    }

}
