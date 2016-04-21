package com.kaaterskil.workflow.engine;

import com.kaaterskil.workflow.engine.delegate.DelegateToken;
import com.kaaterskil.workflow.engine.delegate.JavaDelegate;

public class TestBehavior1 implements JavaDelegate {

    @Override
    public void execute(DelegateToken token) {
        System.out.println("TestBehavior1#execute() called. We're doing something!");
    }

}
