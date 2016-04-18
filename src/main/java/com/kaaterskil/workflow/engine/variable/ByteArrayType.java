package com.kaaterskil.workflow.engine.variable;

public class ByteArrayType implements VariableType {

    @Override
    public String getType() {
        return VariableType.BYTE_ARRAY_TYPE;
    }

    @Override
    public boolean supports(Object value) {
        if(value == null) {
            return true;
        }
        return byte[].class.isAssignableFrom(value.getClass());
    }

    @Override
    public Object getValue(VariableAccessors variable) {
        return variable.getByteValue();
    }

    @Override
    public void setValue(Object value, VariableAccessors variable) {
        variable.setByteValue((byte[]) value);
    }

}
