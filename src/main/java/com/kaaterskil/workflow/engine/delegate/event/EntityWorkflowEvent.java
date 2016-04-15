package com.kaaterskil.workflow.engine.delegate.event;

public class EntityWorkflowEvent extends BaseWorkflowEvent {

    protected final Object entity;

    public EntityWorkflowEvent(WorkflowEventType type, Object entity) {
        super(type);
        if(entity == null) {
            throw new IllegalArgumentException("entity cannot be null");
        }
        this.entity = entity;
    }

    public Object getEntity() {
        return entity;
    }
}
