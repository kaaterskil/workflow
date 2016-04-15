package com.kaaterskil.workflow.engine.delegate.event;

public interface WorkflowEvent {

    WorkflowEventType getType();

    Long getTokenId();

    Long getProcessInstanceId();

    Long getProcessDefinitionId();
}
