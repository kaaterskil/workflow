package com.kaaterskil.workflow.engine.executor;

import com.kaaterskil.workflow.engine.interceptor.CommandContext;
import com.kaaterskil.workflow.engine.persistence.entity.JobEntity;
import com.kaaterskil.workflow.engine.persistence.entity.Token;

public interface JobHandler {
    public static String ASYNC_TYPE = "async";
    public static String EVENT_TYPE = "event";

    String getType();

    void execute(JobEntity job, Long config, Token execution,
            CommandContext commandContext);
}
