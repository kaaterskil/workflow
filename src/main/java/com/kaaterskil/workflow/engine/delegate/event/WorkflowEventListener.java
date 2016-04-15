package com.kaaterskil.workflow.engine.delegate.event;

public interface WorkflowEventListener {

    void onEvent(WorkflowEvent event);

    boolean isFailOnException();
}
