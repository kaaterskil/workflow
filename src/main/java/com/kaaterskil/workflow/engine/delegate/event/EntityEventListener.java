package com.kaaterskil.workflow.engine.delegate.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EntityEventListener implements WorkflowEventListener {
    private static final Logger log = LoggerFactory.getLogger(EntityEventListener.class);

    protected Class<?> entityClazz;
    protected boolean failOnException;

    public EntityEventListener() {
        this(null, true);
    }

    public EntityEventListener(boolean failOnException) {
        this(null, failOnException);
    }

    public EntityEventListener(Class<?> entityClazz, boolean failOnException) {
        this.entityClazz = entityClazz;
        this.failOnException = failOnException;
    }

    @Override
    public void onEvent(WorkflowEvent event) {
        if (isValid(event)) {
            if (event.getType().equals(WorkflowEventType.ENTITY_CREATED)) {
                onCreate(event);
            } else if (event.getType().equals(WorkflowEventType.ENTITY_INITIALIZED)) {
                onInitialized(event);
            } else if (event.getType().equals(WorkflowEventType.ENTITY_DELETED)) {
                onDelete(event);
            } else if (event.getType().equals(WorkflowEventType.ENTITY_UPDATED)) {
                onUpdate(event);
            } else {
                onEntityEvent(event);
            }
        }
    }

    protected boolean isValid(WorkflowEvent event) {
        if (event instanceof EntityWorkflowEvent) {
            if (entityClazz == null) {
                return true;
            } else {
                return entityClazz
                        .isAssignableFrom(((EntityWorkflowEvent) event).getEntity().getClass());
            }
        }
        return false;
    }

    protected void onCreate(WorkflowEvent event) {
        log.debug("onCreate event called");
    }

    protected void onInitialized(WorkflowEvent event) {
        log.debug("onInitialized event called");
    }

    protected void onDelete(WorkflowEvent event) {
        log.debug("onDelete event called");
    }

    protected void onUpdate(WorkflowEvent event) {
        log.debug("onUpdate event called");
    }

    protected void onEntityEvent(WorkflowEvent event) {
        log.debug("onEntityEvent event called");
    }

    /*---------- Getter/Setters ----------*/

    public Class<?> getEntityClazz() {
        return entityClazz;
    }

    public void setEntityClazz(Class<?> entityClazz) {
        this.entityClazz = entityClazz;
    }

    @Override
    public boolean isFailOnException() {
        return failOnException;
    }

    public void setFailOnException(boolean failOnException) {
        this.failOnException = failOnException;
    }

}
