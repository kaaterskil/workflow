package com.kaaterskil.workflow.engine.bpm;

import com.kaaterskil.workflow.engine.delegate.event.WorkflowEvent;
import com.kaaterskil.workflow.engine.delegate.event.WorkflowEventListener;
import com.kaaterskil.workflow.engine.util.ApplicationContextUtil;

public class DelegateWorkflowEventListener extends AbstractDelegateEventListener {

    protected String className;
    protected WorkflowEventListener delegateInstance;
    protected boolean failOnException = true;

    public DelegateWorkflowEventListener(String className, Class<?> entityClazz) {
        this.className = className;
        setEntityClazz(entityClazz);
    }

    public DelegateWorkflowEventListener() {
    }

    @Override
    public void onEvent(WorkflowEvent event) {
        if (isValid(event)) {
            getDelegateInstance().onEvent(event);
        }
    }

    /*---------- Getter/Setters ----------*/

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public WorkflowEventListener getDelegateInstance() {
        if (delegateInstance == null) {
            delegateInstance = ApplicationContextUtil.instantiate(className,
                    WorkflowEventListener.class);
        }
        return delegateInstance;
    }

    public void setDelegateInstance(WorkflowEventListener delegateInstance) {
        this.delegateInstance = delegateInstance;
    }

    @Override
    public boolean isFailOnException() {
        if (delegateInstance != null) {
            return delegateInstance.isFailOnException();
        }
        return failOnException;
    }

}
