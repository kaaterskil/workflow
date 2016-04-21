package com.kaaterskil.workflow.engine.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kaaterskil.workflow.engine.delegate.event.WorkflowEventFactory;
import com.kaaterskil.workflow.engine.delegate.event.WorkflowEventType;
import com.kaaterskil.workflow.engine.interceptor.CommandContext;
import com.kaaterskil.workflow.engine.persistence.entity.JobEntity;

public class ExecuteAsyncJobCommand implements Command<Object> {
    private static final Logger log = LoggerFactory.getLogger(ExecuteAsyncJobCommand.class);

    private final JobEntity job;

    public ExecuteAsyncJobCommand(JobEntity job) {
        this.job = job;
    }

    @Override
    public Object execute(CommandContext commandContext) {
        if (job == null) {
            throw new IllegalArgumentException("Job is null");
        }

        log.debug("Executing asynchronous job {}", job.getId());

        commandContext.getJobService().execute(job);

        commandContext.getEventDispatcher().dispatchEvent(WorkflowEventFactory
                .createEntityEvent(WorkflowEventType.JOB_EXECUTION_SUCCESS, job));

        return null;
    }
}
