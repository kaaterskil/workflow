package com.kaaterskil.workflow.engine.command;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.kaaterskil.workflow.engine.executor.AcquiredJobs;
import com.kaaterskil.workflow.engine.executor.AsyncExecutor;
import com.kaaterskil.workflow.engine.interceptor.CommandContext;
import com.kaaterskil.workflow.engine.persistence.entity.JobEntity;

public class AcquireAsyncJobsDueCommand implements Command<AcquiredJobs> {

    private final AsyncExecutor asyncExecutor;

    public AcquireAsyncJobsDueCommand(AsyncExecutor asyncExecutor) {
        this.asyncExecutor = asyncExecutor;
    }

    @Override
    public AcquiredJobs execute(CommandContext commandContext) {
        final AcquiredJobs acquiredJobs = new AcquiredJobs();

        final List<JobEntity> jobs = commandContext.getJobService().findAsyncJobsDueToExecute();
        if(jobs != null) {
            for(final JobEntity job : jobs) {
                lockJob(commandContext, job, asyncExecutor.getJobLockTimeMillis());
                acquiredJobs.addJob(job);
            }
        }

        return acquiredJobs;
    }

    protected void lockJob(CommandContext commandContext, JobEntity job, int lockTimeInMillis) {
        final Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.MILLISECOND, lockTimeInMillis);
        job.setLockExpiresAt(c.getTime());
    }

}
