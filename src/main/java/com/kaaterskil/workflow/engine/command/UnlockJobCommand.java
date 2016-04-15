package com.kaaterskil.workflow.engine.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kaaterskil.workflow.engine.interceptor.CommandContext;
import com.kaaterskil.workflow.engine.persistence.entity.JobEntity;
import com.kaaterskil.workflow.engine.persistence.entity.Token;

public class UnlockJobCommand implements Command<Object> {
    private static final Logger log = LoggerFactory.getLogger(UnlockJobCommand.class);

    protected JobEntity job;

    public UnlockJobCommand(JobEntity job) {
        this.job = job;
    }

    @Override
    public Object execute(CommandContext commandContext) {
        if (job == null) {
            throw new IllegalArgumentException("Job is null");
        }

        log.debug("Executing UnlockJobCommand for job {}", job.getId());

        if (job.getProcessInstanceId() != null) {
            final Token token = commandContext.getTokenService().findById(job.getProcessInstanceId());
            commandContext.getTokenService().clearProcessInstanceLockTime(token.getId());
        }
        return null;
    }

}
