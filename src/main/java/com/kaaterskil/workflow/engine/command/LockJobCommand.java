package com.kaaterskil.workflow.engine.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kaaterskil.workflow.engine.interceptor.CommandContext;
import com.kaaterskil.workflow.engine.persistence.entity.JobEntity;
import com.kaaterskil.workflow.engine.persistence.entity.Token;

public class LockJobCommand implements Command<Object> {
    private static final Logger log = LoggerFactory.getLogger(LockJobCommand.class);

    protected JobEntity job;

    public LockJobCommand(JobEntity job) {
        this.job = job;
    }

    @Override
    public Object execute(CommandContext commandContext) {
        if (job == null) {
            throw new IllegalArgumentException("Job is null");
        }

        log.debug("Executing LockJobCommand for job {} and execution {}", job.getId(),
                job.getTokenId());

        if (job.getTokenId() != null) {
            final Token token = commandContext.getTokenService().findById(job.getTokenId());
            if (token != null) {
                commandContext.getTokenService()
                        .updateProcessInstanceLockTime(token.getProcessInstanceId());
            }
        }
        return null;
    }

}
