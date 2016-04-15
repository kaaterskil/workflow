package com.kaaterskil.workflow.engine.executor;

import com.kaaterskil.workflow.engine.interceptor.CommandContext;
import com.kaaterskil.workflow.engine.persistence.entity.JobEntity;
import com.kaaterskil.workflow.engine.persistence.entity.Token;

public class AsyncJobHandler implements JobHandler {

    @Override
    public String getType() {
        return JobHandler.ASYNC_TYPE;
    }

    @Override
    public void execute(JobEntity job, Long config, Token token,
            CommandContext commandContext) {
        commandContext.getWorkflow().addContinueProcessSynchronousExecution(token);
    }

}
