package com.kaaterskil.workflow.engine.executor;

import com.kaaterskil.workflow.engine.interceptor.CommandExecutor;
import com.kaaterskil.workflow.engine.persistence.entity.JobEntity;

public interface AsyncExecutor {

    CommandExecutor getCommandExecutor();

    void setCommandExecutor(CommandExecutor commandExecutor);

    int getJobLockTimeMillis();

    int getJobAcquireWaitTimeMillis();

    int getRetryWaitTimeMillis();

    boolean isActive();

    void start();

    void shutdown();

    void executeAsyncJob(JobEntity job);
}
