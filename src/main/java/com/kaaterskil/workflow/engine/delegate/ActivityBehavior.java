package com.kaaterskil.workflow.engine.delegate;

public interface ActivityBehavior {

    void execute(DelegateToken token);
}
