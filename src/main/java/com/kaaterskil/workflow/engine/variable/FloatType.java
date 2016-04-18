package com.kaaterskil.workflow.engine.variable;

public class FloatType implements VariableType {

    @Override
    public String getType() {
        return VariableType.FLOAT_TYPE;
    }

    @Override
    public boolean supports(Object value) {
        if(value == null) {
            return true;
        }
        return Float.class.isAssignableFrom(value.getClass())
                || float.class.isAssignableFrom(value.getClass());
    }

    @Override
    public Object getValue(VariableAccessors variable) {
        return variable.getFloatValue();
    }

    @Override
    public void setValue(Object value, VariableAccessors variable) {
        variable.setFloatValue((Float) value);
        if (value != null) {
            variable.setStringValue(value.toString());
        } else {
            variable.setStringValue(null);
        }

    }
}
