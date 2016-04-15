package com.kaaterskil.workflow.engine.bpm;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import org.springframework.beans.BeansException;

import com.kaaterskil.workflow.engine.exception.WorkflowException;
import com.kaaterskil.workflow.engine.parser.FieldDeclaration;
import com.kaaterskil.workflow.engine.util.ApplicationContextUtil;
import com.kaaterskil.workflow.util.ReflectionUtil;

public class ClassDelegateUtil {

    public static Object createDelegate(String className,
            List<FieldDeclaration> fieldDeclarations) {
        try {
            final Object object = ApplicationContextUtil.getBean(className);
            applyFieldDeclarations(fieldDeclarations, object);
            return object;
        } catch (final BeansException e) {
            throw new WorkflowException("Could not instantiate bean of type " + className);
        }
    }

    public static void applyFieldDeclarations(List<FieldDeclaration> fieldDeclarations,
            Object target) {
        if (fieldDeclarations != null) {
            for (final FieldDeclaration declaration : fieldDeclarations) {
                applyFieldDeclaration(declaration, target);
            }
        }
    }

    public static void applyFieldDeclaration(FieldDeclaration declaration, Object target) {
        final Method setter = ReflectionUtil.getSetter(declaration.getName(), target.getClass(),
                declaration.getValue().getClass());
        if (setter != null) {
            try {
                setter.invoke(target, declaration.getValue());
            } catch (final IllegalAccessException e) {
                throw new WorkflowException("Illegal access while invoking " + declaration.getName()
                        + " on class " + target.getClass().getName(), e);
            } catch (final IllegalArgumentException e) {
                throw new WorkflowException("Error while invoking " + declaration.getName()
                        + " on class " + target.getClass().getName(), e);
            } catch (final InvocationTargetException e) {
                throw new WorkflowException("Exception while invoking " + declaration.getName()
                        + " on class " + target.getClass().getName(), e);
            }
        }
    }
}
