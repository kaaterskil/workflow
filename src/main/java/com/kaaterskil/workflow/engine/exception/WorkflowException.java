package com.kaaterskil.workflow.engine.exception;

public class WorkflowException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public WorkflowException(String message, Throwable cause) {
        super(message, cause);
    }

    public WorkflowException(String message) {
        super(message);
    }

    public WorkflowException(Throwable cause) {
        super(cause);
    }

}
