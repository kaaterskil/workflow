package com.kaaterskil.workflow.engine.persistence.mapper;

import com.kaaterskil.workflow.engine.persistence.entity.VariableEntity;

public class VariableMapper {

    public static VariableEntity toEntity(String variableName, Object value) {
        if(variableName == null) {
            return null;
        }

        final VariableEntity entity = new VariableEntity();
        entity.setName(variableName);
        if(value != null) {
            if(value instanceof Long) {
                entity.setLongValue((Long) value);
                entity.setType(Long.class.getName());
            } else if(value instanceof Float || value instanceof Double) {
                entity.setFloatValue((Float) value);
                entity.setType(Float.class.getName());
            } else if(value instanceof Integer || value instanceof Short) {
                entity.setIntValue((Integer) value);
                entity.setType(Integer.class.getName());
            } else if(value instanceof Boolean) {
                entity.setBooleanValue((Boolean) value);
                entity.setType(Boolean.class.getName());
            } else if(value instanceof String) {
                entity.setStringValue((String) value);
                entity.setType(String.class.getName());
            } else {
                // TODO We need a better way to serialize the value
                entity.setStringValue(value.toString());
                entity.setType(entity.getClass().getCanonicalName());
            }
        }

        return entity;
    }

    public static Object toInstance(VariableEntity entity) {
        if(entity == null) {
            return null;
        }

        Object value = null;
        final String type = entity.getType();
        if(type.equals(Boolean.class.getName())) {
            value = new Boolean(entity.isBooleanValue());
        } else if (type.equals(Integer.class.getName())) {
            value = new Integer(entity.getIntValue());
        } else if(type.equals(Float.class.getName())) {
            value = new Float(entity.getFloatValue());
        } else if(type.equals(Long.class.getName())) {
            value = new Long(entity.getLongValue());
        } else if (type.equals(String.class.getName())) {
            value = entity.getStringValue();
        } else {
            // TODO Deserialize the value by the className
        }
        return value;
    }
}
