package com.kaaterskil.workflow.engine.runtime;

public interface ProcessInstance extends Execution {

    Long getProcessDefinitionId();

    String getProcessDefinitionKey();

    String getProcessDefinitionName();

    int getProcessDefinitionVersion();
}
