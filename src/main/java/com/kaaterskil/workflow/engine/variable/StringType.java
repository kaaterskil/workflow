package com.kaaterskil.workflow.engine.variable;

public class StringType implements VariableType {

    @Override
    public String getType() {
        return VariableType.STRING_TYPE;
    }

    @Override
    public boolean supports(Object value) {
        if(value == null) {
            return true;
        }
        return String.class.isAssignableFrom(value.getClass());
    }

    @Override
    public Object getValue(VariableAccessors variable) {
        return variable.getStringValue();
    }

    @Override
    public void setValue(Object value, VariableAccessors variable) {
        variable.setStringValue((String) value);
    }

}
