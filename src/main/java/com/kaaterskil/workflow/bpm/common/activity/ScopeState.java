package com.kaaterskil.workflow.bpm.common.activity;

public enum ScopeState {

    ACTIVATED,
    IN_EXECUTION,
    COMPLETED,
    IN_COMPENSATION,
    COMPENSATION,
    IN_ERROR,
    IN_CANCELLATION,
    CANCELLED;
}
