package com.kaaterskil.workflow.engine.variable;

public class BooleanType implements VariableType {

    @Override
    public String getType() {
        return VariableType.BOOLEAN_TYPE;
    }

    @Override
    public boolean supports(Object value) {
        if (value == null) {
            return true;
        }
        return Boolean.class.isAssignableFrom(value.getClass())
                || boolean.class.isAssignableFrom(value.getClass());
    }

    @Override
    public Object getValue(VariableAccessors variable) {
        return variable.getBooleanValue();
    }

    @Override
    public void setValue(Object value, VariableAccessors variable) {
        if (value == null) {
            variable.setLongValue(null);
            variable.setBooleanValue(Boolean.FALSE);
        } else {
            final Boolean booleanValue = (Boolean) value;
            if (booleanValue) {
                variable.setLongValue(1L);
            } else {
                variable.setLongValue(0L);
            }
            variable.setBooleanValue(booleanValue);
        }
    }

}
