package com.kaaterskil.workflow.engine.delegate.event;

public abstract class AbstractDelegateEventListener implements WorkflowEventListener {

    protected Class<?> entityClazz;

    public Class<?> getEntityClazz() {
        return entityClazz;
    }

    public void setEntityClazz(Class<?> entityClazz) {
        this.entityClazz = entityClazz;
    }

    protected boolean isValid(WorkflowEvent event) {
        if (entityClazz != null) {
            if (event instanceof EntityWorkflowEvent) {
                final Object entity = ((EntityWorkflowEvent) event).getEntity();
                if (entity != null) {
                    return entityClazz.isAssignableFrom(entity.getClass());
                }
            }
        } else {
            return true;
        }
        return false;
    }

}
