package com.kaaterskil.workflow.engine.variable;

public class LongType implements VariableType {

    @Override
    public String getType() {
        return VariableType.LONG_TYPE;
    }

    @Override
    public boolean supports(Object value) {
        if(value == null) {
            return true;
        }
        return Long.class.isAssignableFrom(value.getClass())
                || long.class.isAssignableFrom(value.getClass());
    }

    @Override
    public Object getValue(VariableAccessors variable) {
        return variable.getLongValue();
    }

    @Override
    public void setValue(Object value, VariableAccessors variable) {
        variable.setLongValue((Long) value);
        if (value != null) {
            variable.setStringValue(value.toString());
        } else {
            variable.setStringValue(null);
        }
    }

}
