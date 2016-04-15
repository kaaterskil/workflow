package com.kaaterskil.workflow.util;

import java.lang.reflect.Method;

import com.kaaterskil.workflow.engine.exception.WorkflowException;

public class ReflectionUtil {

    public static Method getSetter(String fieldName, Class<?> clazz, Class<?> fieldType) {
        final String setterName = "set" + Character.toTitleCase(fieldName.charAt(0))
                + fieldName.substring(1, fieldName.length());

        try {
            final Method[] methods = clazz.getMethods();
            for (final Method method : methods) {
                if (method.getName().equals(setterName)) {
                    final Class<?>[] paramTypes = method.getParameterTypes();
                    if (paramTypes != null && paramTypes.length == 1
                            && paramTypes[0].isAssignableFrom(fieldType)) {
                        return method;
                    }
                }
            }
            return null;
        } catch (final SecurityException e) {
            throw new WorkflowException("Could not access method " + setterName + " on class "
                    + clazz.getCanonicalName());
        }
    }
}
