package com.kaaterskil.workflow.engine.delegate.event;

public class EntityExceptionWorkflowEvent extends EntityWorkflowEvent {

    private Throwable cause;

    public EntityExceptionWorkflowEvent(WorkflowEventType type, Object entity, Throwable cause) {
        super(type, entity);
        this.cause = cause;
    }

    public Throwable getCause() {
        return cause;
    }

    public void setCause(Throwable cause) {
        this.cause = cause;
    }
}
