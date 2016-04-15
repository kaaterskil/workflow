package com.kaaterskil.workflow.engine.executor;

import java.util.LinkedList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kaaterskil.workflow.engine.interceptor.CommandExecutor;
import com.kaaterskil.workflow.engine.persistence.entity.JobEntity;

public class BaseAsyncExecutor implements AsyncExecutor {
    private static final Logger log = LoggerFactory.getLogger(BaseAsyncExecutor.class);

    protected int corePoolSize = 2;
    protected int maxPoolSize = 10;
    protected long keepAliveTime = 5000L;
    protected int queueSize = 100;
    protected BlockingQueue<Runnable> threadPoolQueue;
    protected ExecutorService executorService;

    protected long secondsToWaitOnShutdown = 30L;
    protected Thread asyncJobAcquisitionThread;
    protected AcquireAsyncJobsDueRunnable asyncJobsDueRunnable;

    protected CommandExecutor commandExecutor;
    protected boolean isActive;
    protected int jobLockTimeMillis = 5 * 60 * 1000;
    protected int jobAcquireWaitTimeMillis = 10 * 1000;
    protected int retryWaitTimeMillis = 50;
    protected LinkedList<JobEntity> temporaryJobQueue = new LinkedList<>();

    @Override
    public void executeAsyncJob(JobEntity job) {
        Runnable runnable = null;
        if (isActive) {
            runnable = new ExecuteAsyncRunnable(job, commandExecutor);
            executorService.execute(runnable);
        } else {
            temporaryJobQueue.add(job);
        }
    }

    @Override
    public void start() {
        if (isActive) {
            return;
        }

        log.info("Starting up the async job executor");
        if (asyncJobsDueRunnable == null) {
            asyncJobsDueRunnable = new AcquireAsyncJobsDueRunnable(this);
        }
        startExecutingAsyncJobs();

        isActive = true;
        while (!temporaryJobQueue.isEmpty()) {
            final JobEntity job = temporaryJobQueue.pop();
            executeAsyncJob(job);
        }
        isActive = true;
    }

    @Override
    public synchronized void shutdown() {
        if (!isActive) {
            return;
        }

        log.info("Shutting down the async job executor");
        asyncJobsDueRunnable.stop();
        stopExecutingAsyncJobs();

        asyncJobsDueRunnable = null;
        isActive = false;
    }

    protected void startExecutingAsyncJobs() {
        if (threadPoolQueue == null) {
            log.debug("Creating thread pool queue of size {}", queueSize);
            threadPoolQueue = new ArrayBlockingQueue<Runnable>(queueSize);
        }

        if (executorService == null) {
            log.info(
                    "Creating executor service with corePoolSize {}, maxPoolSize {} and keepAliveTime {}",
                    corePoolSize, maxPoolSize, keepAliveTime);

            final ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize,
                    maxPoolSize, keepAliveTime, TimeUnit.MILLISECONDS, threadPoolQueue);
            threadPoolExecutor
                    .setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
            executorService = threadPoolExecutor;
        }
        startJobAcquisitionThread();
    }

    protected void startJobAcquisitionThread() {
        if (asyncJobAcquisitionThread == null) {
            asyncJobAcquisitionThread = new Thread(asyncJobsDueRunnable);
        }
        asyncJobAcquisitionThread.start();
    }

    protected void stopExecutingAsyncJobs() {
        stopJobAcquisitionThread();
        executorService.shutdown();

        try {
            if (!executorService.awaitTermination(secondsToWaitOnShutdown, TimeUnit.SECONDS)) {
                log.warn(
                        "Timeout during shutdown of async job executor. Current running jobs could not end within "
                                + secondsToWaitOnShutdown + " seconds after shutdown operation.");
            }
        } catch (final InterruptedException e) {
            log.warn("Interrupted while shutting down the async job executor. ", e);
        }

        executorService = null;
    }

    protected void stopJobAcquisitionThread() {
        try {
            asyncJobAcquisitionThread.join();
        } catch (final InterruptedException e) {
            log.warn("Interrupted while waiting for the async job acquisition thread to terminate");
        }
        asyncJobAcquisitionThread = null;
    }

    /*---------- Getter/Setters ----------*/

    public int getCorePoolSize() {
        return corePoolSize;
    }

    public void setCorePoolSize(int corePoolSize) {
        this.corePoolSize = corePoolSize;
    }

    public int getMaxPoolSize() {
        return maxPoolSize;
    }

    public void setMaxPoolSize(int maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }

    public long getKeepAliveTime() {
        return keepAliveTime;
    }

    public void setKeepAliveTime(long keepAliveTime) {
        this.keepAliveTime = keepAliveTime;
    }

    public int getQueueSize() {
        return queueSize;
    }

    public void setQueueSize(int queueSize) {
        this.queueSize = queueSize;
    }

    public BlockingQueue<Runnable> getThreadPoolQueue() {
        return threadPoolQueue;
    }

    public void setThreadPoolQueue(BlockingQueue<Runnable> threadPoolQueue) {
        this.threadPoolQueue = threadPoolQueue;
    }

    public ExecutorService getExecutorService() {
        return executorService;
    }

    public void setExecutorService(ExecutorService executorService) {
        this.executorService = executorService;
    }

    @Override
    public CommandExecutor getCommandExecutor() {
        return commandExecutor;
    }

    @Override
    public void setCommandExecutor(CommandExecutor commandExecutor) {
        this.commandExecutor = commandExecutor;
    }

    public long getSecondsToWaitOnShutdown() {
        return secondsToWaitOnShutdown;
    }

    public void setSecondsToWaitOnShutdown(long secondsToWaitOnShutdown) {
        this.secondsToWaitOnShutdown = secondsToWaitOnShutdown;
    }

    @Override
    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    @Override
    public int getJobLockTimeMillis() {
        return jobLockTimeMillis;
    }

    public void setJobLockTimeMillis(int jobLockTimeMillis) {
        this.jobLockTimeMillis = jobLockTimeMillis;
    }

    @Override
    public int getJobAcquireWaitTimeMillis() {
        return jobAcquireWaitTimeMillis;
    }

    public void setJobAcquireWaitTimeMillis(int jobAcquireWaitTimeMillis) {
        this.jobAcquireWaitTimeMillis = jobAcquireWaitTimeMillis;
    }

    public int getRetryWaitTimeMillis() {
        return retryWaitTimeMillis;
    }

    public void setRetryWaitTimeMillis(int retryWaitTimeMillis) {
        this.retryWaitTimeMillis = retryWaitTimeMillis;
    }

    public LinkedList<JobEntity> getTemporaryJobQueue() {
        return temporaryJobQueue;
    }

    public void setTemporaryJobQueue(LinkedList<JobEntity> temporaryJobQueue) {
        this.temporaryJobQueue = temporaryJobQueue;
    }

}
