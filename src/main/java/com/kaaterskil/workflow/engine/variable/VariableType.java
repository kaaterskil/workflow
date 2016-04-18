package com.kaaterskil.workflow.engine.variable;

public interface VariableType {
    public static final String BOOLEAN_TYPE = "boolean";
    public static final String INTEGER_TYPE = "integer";
    public static final String LONG_TYPE = "long";
    public static final String FLOAT_TYPE = "float";
    public static final String STRING_TYPE = "string";
    public static final String BYTE_ARRAY_TYPE = "byteArray";
    public static final String DATE_TYPE = "date";

    String getType();

    boolean supports(Object value);

    Object getValue(VariableAccessors variable);

    void setValue(Object value, VariableAccessors variable);
}
