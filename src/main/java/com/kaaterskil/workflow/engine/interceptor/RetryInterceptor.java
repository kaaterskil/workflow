package com.kaaterskil.workflow.engine.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kaaterskil.workflow.engine.command.Command;
import com.kaaterskil.workflow.engine.exception.WorkflowException;

public class RetryInterceptor extends CommandInterceptor {
    private static final Logger log = LoggerFactory.getLogger(RetryInterceptor.class);

    protected int maxRetries = 3;
    protected int waitMillis = 50;
    protected int waitIncreaseFactor = 5;

    @Override
    public <T> T execute(Command<T> command) {
        long waitTime = waitMillis;
        int failedAttempts = 0;

        do {
            if (failedAttempts > 0) {
                log.debug("Waiting for {}ms before retrying the command", waitTime);
                waitBeforeRetry(waitTime);
                waitTime *= waitIncreaseFactor;
            }

            try {
                return next.execute(command);
            } catch (final Exception e) {
                log.debug("Caught exception: " + e);
            }

            failedAttempts++;
        } while (failedAttempts < maxRetries);

        throw new WorkflowException(maxRetries + " retries failed. Giving up.");
    }

    protected void waitBeforeRetry(long waitTime) {
        try {
            Thread.sleep(waitTime);
        } catch (final InterruptedException e) {
            log.debug("Interrupted while waiting for a retry");
        }
    }

    /*---------- Getter/Setters ----------*/

    public int getMaxRetries() {
        return maxRetries;
    }

    public void setMaxRetries(int maxRetries) {
        this.maxRetries = maxRetries;
    }

    public int getWaitMillis() {
        return waitMillis;
    }

    public void setWaitMillis(int waitMillis) {
        this.waitMillis = waitMillis;
    }

    public int getWaitIncreaseFactor() {
        return waitIncreaseFactor;
    }

    public void setWaitIncreaseFactor(int waitIncreaseFactor) {
        this.waitIncreaseFactor = waitIncreaseFactor;
    }

}
