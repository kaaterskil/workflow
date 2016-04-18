package com.kaaterskil.workflow.engine.variable;

public interface VariableAccessors {

    String getName();

    Boolean getBooleanValue();

    void setBooleanValue(Boolean booleanValue);

    Integer getIntValue();

    void setIntValue(Integer integerValue);

    Long getLongValue();

    void setLongValue(Long longValue);

    Float getFloatValue();

    void setFloatValue(Float floatValue);

    String getStringValue();

    void setStringValue(String stringValue);

    byte[] getByteValue();

    void setByteValue(byte[] byteValue);
}
