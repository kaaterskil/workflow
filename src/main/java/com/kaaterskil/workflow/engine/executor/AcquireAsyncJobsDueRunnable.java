package com.kaaterskil.workflow.engine.executor;

import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kaaterskil.workflow.engine.command.AcquireAsyncJobsDueCommand;
import com.kaaterskil.workflow.engine.exception.WorkflowOptimisticLockingException;
import com.kaaterskil.workflow.engine.interceptor.CommandExecutor;
import com.kaaterskil.workflow.engine.persistence.entity.JobEntity;

public class AcquireAsyncJobsDueRunnable implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(AcquireAsyncJobsDueRunnable.class);

    private final AsyncExecutor asyncExecutor;
    private final Object MONITOR = new Object();
    private long millisToWait;
    private boolean isInterrupted;
    private final AtomicBoolean isWaiting = new AtomicBoolean(false);

    public AcquireAsyncJobsDueRunnable(AsyncExecutor asyncExecutor) {
        this.asyncExecutor = asyncExecutor;
    }

    @Override
    public void run() {
        final CommandExecutor commandExecutor = asyncExecutor.getCommandExecutor();

        while (!isInterrupted) {
            try {
                final AcquiredJobs acquiredJobs = commandExecutor
                        .execute(new AcquireAsyncJobsDueCommand(asyncExecutor));
                for (final JobEntity job : acquiredJobs.getAcquiredJobs()) {
                    asyncExecutor.executeAsyncJob(job);
                }

                millisToWait = asyncExecutor.getJobAcquireWaitTimeMillis();
                if (acquiredJobs.size() > 1) {
                    millisToWait = 0;
                }

            } catch (final WorkflowOptimisticLockingException e) {
                log.debug("Optimistic locking exception during async job acquisition: "
                        + e.getMessage());
            } catch (final Exception e) {
                log.error("Exception during async job acquisition: {}", e.getMessage());
                millisToWait = asyncExecutor.getJobAcquireWaitTimeMillis();
            }

            if (millisToWait > 0) {
                try {
                    log.debug("Async job acquisition thread sleeping for {} milliseconds",
                            millisToWait);

                    synchronized (MONITOR) {
                        if (!isInterrupted) {
                            isWaiting.set(true);
                            MONITOR.wait(millisToWait);
                        }
                    }

                    log.debug("Async job acquisition thread woke up");
                } catch (final InterruptedException e) {
                    log.debug("Async job acquisition thread interrupted");
                } finally {
                    isWaiting.set(false);
                }
            }
        }
        log.info("Stopped async job acquisition");
    }

    public void stop() {
        synchronized (MONITOR) {
            isInterrupted = true;
            if (isWaiting.compareAndSet(true, false)) {
                MONITOR.notifyAll();
            }
        }
    }

    /*---------- Getter/Setters ----------*/

    public long getMillisToWait() {
        return millisToWait;
    }

    public void setMillisToWait(long millisToWait) {
        this.millisToWait = millisToWait;
    }

}
