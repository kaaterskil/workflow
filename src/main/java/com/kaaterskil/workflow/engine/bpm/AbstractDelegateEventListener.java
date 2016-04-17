package com.kaaterskil.workflow.engine.bpm;

import com.kaaterskil.workflow.engine.delegate.event.EntityWorkflowEvent;
import com.kaaterskil.workflow.engine.delegate.event.WorkflowEvent;
import com.kaaterskil.workflow.engine.delegate.event.WorkflowEventListener;

public abstract class AbstractDelegateEventListener implements WorkflowEventListener {

    protected Class<?> entityClazz;

    protected boolean isValid(WorkflowEvent event) {
        boolean isValid = false;
        if(entityClazz != null) {
            final Object instance = ((EntityWorkflowEvent) event).getEntity();
            if(instance != null) {
                isValid = entityClazz.isAssignableFrom(instance.getClass());
            }
        } else {
            isValid = true;
        }
        return isValid;
    }

    /*---------- Getter/Setters ----------*/

    public Class<?> getEntityClazz() {
        return entityClazz;
    }

    public void setEntityClazz(Class<?> entityClazz) {
        this.entityClazz = entityClazz;
    }
}
