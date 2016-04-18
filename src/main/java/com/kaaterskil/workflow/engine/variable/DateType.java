package com.kaaterskil.workflow.engine.variable;

import java.util.Date;

public class DateType implements VariableType {

    @Override
    public String getType() {
        return VariableType.DATE_TYPE;
    }

    @Override
    public boolean supports(Object value) {
        if (value == null) {
            return true;
        }
        return Date.class.isAssignableFrom(value.getClass());
    }

    @Override
    public Object getValue(VariableAccessors variable) {
        final Long millis = variable.getLongValue();
        if (millis != null) {
            return new Date(millis);
        }
        return null;
    }

    @Override
    public void setValue(Object value, VariableAccessors variable) {
        if(value != null) {
            variable.setLongValue(((Date) value).getTime());
        } else {
            variable.setLongValue(null);
        }

    }

}
