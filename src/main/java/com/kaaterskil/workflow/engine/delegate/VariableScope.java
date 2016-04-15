package com.kaaterskil.workflow.engine.delegate;

import java.util.Map;
import java.util.Set;

public interface VariableScope {

    Map<String, Object> getVariables();

    void setVariables(Map<String, Object> variables);

    Object getVariable(String variableName);

    void setVariable(String variableName, Object value);

    void removeVariable(String variableName);

    Set<String> getVariableNames();

    boolean hasVariables();

    boolean hasVariable(String variableName);
}
