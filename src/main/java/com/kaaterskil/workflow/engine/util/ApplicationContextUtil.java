package com.kaaterskil.workflow.engine.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.kaaterskil.workflow.engine.exception.WorkflowException;

public class ApplicationContextUtil implements ApplicationContextAware {

    private static ApplicationContext CONTEXT;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        CONTEXT = applicationContext;
    }

    public static Object getBean(String name) {
        return CONTEXT.getBean(name);
    }

    public static <T> T instantiate(String className, Class<T> type) {
        try {
            return type.cast(Class.forName(className).newInstance());
        } catch (final InstantiationException e) {
            throw new WorkflowException("Could not instantiate object " + className, e);
        } catch (final IllegalAccessException e) {
            throw new WorkflowException("Could not access object " + className, e);
        } catch (final ClassNotFoundException e) {
            throw new WorkflowException("Could not find class " + className, e);
        }
    }

}
