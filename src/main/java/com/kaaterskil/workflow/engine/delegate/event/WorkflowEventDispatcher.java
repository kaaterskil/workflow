package com.kaaterskil.workflow.engine.delegate.event;

public interface WorkflowEventDispatcher {

    void addEventListener(WorkflowEventListener listener);

    void addEventListener(WorkflowEventListener listener, WorkflowEventType...eventTypes);

    void addTypedEventListener(WorkflowEventListener listener, WorkflowEventType eventType);

    void removeEventListener(WorkflowEventListener listener);

    void dispatchEvent(WorkflowEvent event);
}
