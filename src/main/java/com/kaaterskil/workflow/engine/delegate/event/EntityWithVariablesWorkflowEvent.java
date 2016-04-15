package com.kaaterskil.workflow.engine.delegate.event;

import java.util.Map;

public class EntityWithVariablesWorkflowEvent extends EntityWorkflowEvent {

    private final Map<String, Object> variables;

    public EntityWithVariablesWorkflowEvent(WorkflowEventType type, Object entity,
            Map<String, Object> variables) {
        super(type, entity);
        this.variables = variables;
    }

    public Map<String, Object> getVariables() {
        return variables;
    }
}
