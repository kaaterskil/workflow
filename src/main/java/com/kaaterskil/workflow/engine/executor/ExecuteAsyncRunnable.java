package com.kaaterskil.workflow.engine.executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kaaterskil.workflow.engine.command.Command;
import com.kaaterskil.workflow.engine.command.ExecuteAsyncJobCommand;
import com.kaaterskil.workflow.engine.command.LockJobCommand;
import com.kaaterskil.workflow.engine.command.UnlockJobCommand;
import com.kaaterskil.workflow.engine.exception.WorkflowOptimisticLockingException;
import com.kaaterskil.workflow.engine.interceptor.CommandContext;
import com.kaaterskil.workflow.engine.interceptor.CommandExecutor;
import com.kaaterskil.workflow.engine.persistence.entity.JobEntity;

public class ExecuteAsyncRunnable implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(ExecuteAsyncRunnable.class);

    protected JobEntity job;
    protected CommandExecutor commandExecutor;

    public ExecuteAsyncRunnable(JobEntity job, CommandExecutor commandExecutor) {
        this.job = job;
        this.commandExecutor = commandExecutor;
    }

    @Override
    public void run() {
        try {
            commandExecutor.execute(new LockJobCommand(job));
        } catch (final Throwable lockException) {
            log.debug("Exception during lock acquisition. Retrying job. ",
                    lockException.getMessage());

            commandExecutor.execute(new Command<Void>() {
                @Override
                public Void execute(CommandContext commandContext) {
                    commandContext.getJobService().retryAsyncJob(job);
                    return null;
                }
            });
            return;
        }

        try {
            commandExecutor.execute(new ExecuteAsyncJobCommand(job));
        } catch (final WorkflowOptimisticLockingException e) {
            handleFailedJob(e);
            log.debug("Optimistic locking exception during job execution: {}", e.getMessage());
        } catch (final Throwable t) {
            handleFailedJob(t);
            log.error("job " + job.getId() + " failed");
        }

        try {
            commandExecutor.execute(new UnlockJobCommand(job));
        } catch (final WorkflowOptimisticLockingException e) {
            log.debug("Optimistic locking exception while unlocking the job {}", e.getMessage());
            return;
        } catch (final Throwable t) {
            log.error("Error while unlocking job " + job.getId(), t);
            return;
        }
    }

    protected void handleFailedJob(Throwable exception) {
        commandExecutor.execute(new Command<Void>() {
            @Override
            public Void execute(CommandContext commandContext) {
                // TODO Implement handleFailedJob execution
                return null;
            }
        });
    }

}
