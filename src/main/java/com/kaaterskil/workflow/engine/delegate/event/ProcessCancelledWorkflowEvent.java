package com.kaaterskil.workflow.engine.delegate.event;

public class ProcessCancelledWorkflowEvent extends BaseWorkflowEvent {

    private Object cause;

    public ProcessCancelledWorkflowEvent() {
        super(WorkflowEventType.PROCESS_CANCELLED);
    }

    public Object getCause() {
        return cause;
    }

    public void setCause(Object cause) {
        this.cause = cause;
    }
}
