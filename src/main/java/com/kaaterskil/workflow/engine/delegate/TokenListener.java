package com.kaaterskil.workflow.engine.delegate;

public interface TokenListener {

    String EVENT_START = "start";
    String EVENT_END = "end";
    String EVENT_TRANSITION = "transition";

    void notify(DelegateToken token);
}
