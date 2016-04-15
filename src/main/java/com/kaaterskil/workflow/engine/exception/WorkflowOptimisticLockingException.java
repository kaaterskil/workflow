package com.kaaterskil.workflow.engine.exception;

public class WorkflowOptimisticLockingException extends WorkflowException {
    private static final long serialVersionUID = 1L;

    public WorkflowOptimisticLockingException(String message) {
        super(message);
    }

}
