package com.kaaterskil.workflow.engine.variable;

public class IntegerType implements VariableType {

    @Override
    public String getType() {
        return VariableType.INTEGER_TYPE;
    }

    @Override
    public boolean supports(Object value) {
        if(value == null) {
            return true;
        }
        return Integer.class.isAssignableFrom(value.getClass())
                || int.class.isAssignableFrom(value.getClass());
    }

    @Override
    public Object getValue(VariableAccessors variable) {
        return variable.getIntValue();
    }

    @Override
    public void setValue(Object value, VariableAccessors variable) {
        variable.setIntValue((Integer) value);
        if (value != null) {
            variable.setStringValue(value.toString());
        } else {
            variable.setStringValue(null);
        }
    }

}
