package com.kaaterskil.workflow.engine.runtime;

public interface Execution {

    Long getId();

    Long getParentId();

    Long getProcessInstanceId();

    String getActivityId();

    String getName();

    boolean isEnded();

}
