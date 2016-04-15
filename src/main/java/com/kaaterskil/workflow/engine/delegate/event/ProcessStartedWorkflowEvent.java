package com.kaaterskil.workflow.engine.delegate.event;

import java.util.Map;

public class ProcessStartedWorkflowEvent extends EntityWithVariablesWorkflowEvent {

    // These properties are for super executions (not currently implemented)
    protected Long nestedProcessInstanceId;
    protected Long nestedProcessDefinitionId;

    public ProcessStartedWorkflowEvent(Object entity, Map<String, Object> variables) {
        super(WorkflowEventType.PROCESS_STARTED, entity, variables);

        // TODO implement super execution
        nestedProcessInstanceId = null;
        nestedProcessDefinitionId = null;
    }

    public Long getNestedProcessInstanceId() {
        return nestedProcessInstanceId;
    }

    public void setNestedProcessInstanceId(Long nestedProcessInstanceId) {
        this.nestedProcessInstanceId = nestedProcessInstanceId;
    }

    public Long getNestedProcessDefinitionId() {
        return nestedProcessDefinitionId;
    }

    public void setNestedProcessDefinitionId(Long nestedProcessDefinitionId) {
        this.nestedProcessDefinitionId = nestedProcessDefinitionId;
    }
}
