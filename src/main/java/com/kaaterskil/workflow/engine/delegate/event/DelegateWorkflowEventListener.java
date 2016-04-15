package com.kaaterskil.workflow.engine.delegate.event;

import org.springframework.beans.BeansException;

import com.kaaterskil.workflow.engine.exception.WorkflowException;
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

    protected WorkflowEventListener getDelegateInstance() {
        if (delegateInstance == null) {
            try {
                final Object instance = ApplicationContextUtil.getBean(className);
                if (instance instanceof WorkflowEventListener) {
                    delegateInstance = (WorkflowEventListener) instance;
                } else {
                    throw new IllegalArgumentException(
                            "Class " + className + " does not implement WorkflowEventListener");
                }
            } catch (final BeansException e) {
                throw new WorkflowException("Could not instantiate bean " + className);
            }
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
