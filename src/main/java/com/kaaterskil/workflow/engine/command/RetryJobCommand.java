package com.kaaterskil.workflow.engine.command;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kaaterskil.workflow.engine.context.Context;
import com.kaaterskil.workflow.engine.delegate.event.WorkflowEventDispatcher;
import com.kaaterskil.workflow.engine.delegate.event.WorkflowEventFactory;
import com.kaaterskil.workflow.engine.delegate.event.WorkflowEventType;
import com.kaaterskil.workflow.engine.interceptor.CommandContext;
import com.kaaterskil.workflow.engine.persistence.entity.JobEntity;

public class RetryJobCommand implements Command<Object> {
    private static final Logger log = LoggerFactory.getLogger(RetryJobCommand.class);

    protected Long jobId;
    protected Throwable exception;

    public RetryJobCommand(Long jobId, Throwable exception) {
        this.jobId = jobId;
        this.exception = exception;
    }

    @Override
    public Object execute(CommandContext commandContext) {
        final JobEntity job = commandContext.getJobService().findById(jobId);
        if (job == null) {
            return null;
        }

        log.debug("Decrementing retries for job {}", job.getId());

        job.setRetries(job.getRetries() - 1);
        job.setLockOwner(null);
        job.setLockExpiresAt(null);
        job.setDueBy(calculateDueDate(
                Context.getProcessEngineService().getAsyncFailedJobWaitTimeMillis(), null));
        if (exception != null) {
            job.setExceptionMessage(exception.getMessage());
            job.setExceptionStackTrace(getExceptionStackTrace());
        }

        final WorkflowEventDispatcher dispatcher = Context.getProcessEngineService()
                .getEventDispatcher();
        dispatcher.dispatchEvent(WorkflowEventFactory
                .createEntityEvent(WorkflowEventType.ENTITY_UPDATED, job));
        dispatcher.dispatchEvent(WorkflowEventFactory
                .createEntityEvent(WorkflowEventType.JOB_RETRIES_DECREMENTED, job));

        commandContext.getJobService().save(job);

        return null;
    }

    protected Date calculateDueDate(long waitTimeMillis, Date oldDate) {
        final Calendar c = Calendar.getInstance();
        if (oldDate != null) {
            c.setTime(oldDate);
        } else {
            c.setTime(new Date());
        }
        c.add(Calendar.MILLISECOND, (int) waitTimeMillis);
        return c.getTime();
    }

    protected String getExceptionStackTrace() {
        final StringWriter sw = new StringWriter();
        exception.printStackTrace(new PrintWriter(sw));
        return sw.toString();
    }
}
