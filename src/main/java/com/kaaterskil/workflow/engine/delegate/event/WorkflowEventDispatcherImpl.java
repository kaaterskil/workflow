package com.kaaterskil.workflow.engine.delegate.event;

import org.springframework.stereotype.Component;

@Component
public class WorkflowEventDispatcherImpl implements WorkflowEventDispatcher {

    protected WorkflowEventUtil eventHelper;

    public WorkflowEventDispatcherImpl() {
        eventHelper = new WorkflowEventUtil();
    }

    @Override
    public void addEventListener(WorkflowEventListener listener) {
        eventHelper.addEventListener(listener);
    }

    @Override
    public void addEventListener(WorkflowEventListener listener, WorkflowEventType... eventTypes) {
        eventHelper.addEventListener(listener, eventTypes);
    }

    public void addTypedEventListener(WorkflowEventListener listener, WorkflowEventType type) {
        eventHelper.addTypedEventListener(listener, type);
    }

    @Override
    public void removeEventListener(WorkflowEventListener listener) {
        eventHelper.removeEventListener(listener);
    }

    @Override
    public void dispatchEvent(WorkflowEvent event) {
        eventHelper.dispatchEvent(event);
    }

}
