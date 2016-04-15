package com.kaaterskil.workflow.engine.delegate.event;

public class ActivityCancelledWorkflowEvent extends ActivityWorkflowEvent {

    private Object cause;

    public ActivityCancelledWorkflowEvent() {
        super(WorkflowEventType.ACTIVITY_CANCELLED);
    }

    public Object getCause() {
        return cause;
    }

    public void setCause(Object cause) {
        this.cause = cause;
    }
}
