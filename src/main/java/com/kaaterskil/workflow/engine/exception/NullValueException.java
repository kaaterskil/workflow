package com.kaaterskil.workflow.engine.exception;

public class NullValueException extends WorkflowException {
    private static final long serialVersionUID = 1L;

    public NullValueException(String message, Throwable cause) {
        super(message, cause);
    }

    public NullValueException(String message) {
        super(message);
    }

    public NullValueException(Throwable cause) {
        super(cause);
    }

}
