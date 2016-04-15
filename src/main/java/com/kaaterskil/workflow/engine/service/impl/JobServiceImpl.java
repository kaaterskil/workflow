package com.kaaterskil.workflow.engine.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kaaterskil.workflow.engine.context.Context;
import com.kaaterskil.workflow.engine.delegate.event.WorkflowEventFactory;
import com.kaaterskil.workflow.engine.delegate.event.WorkflowEventType;
import com.kaaterskil.workflow.engine.executor.AsyncExecutor;
import com.kaaterskil.workflow.engine.executor.JobHandler;
import com.kaaterskil.workflow.engine.persistence.entity.JobEntity;
import com.kaaterskil.workflow.engine.persistence.entity.MessageEntity;
import com.kaaterskil.workflow.engine.persistence.entity.TimerEntity;
import com.kaaterskil.workflow.engine.persistence.entity.Token;
import com.kaaterskil.workflow.engine.persistence.repository.JobRepository;
import com.kaaterskil.workflow.engine.service.JobService;

@Component
public class JobServiceImpl implements JobService {

    @Autowired
    private JobRepository repository;

    @Override
    public JobEntity findById(Long jobId) {
        return repository.findOne(jobId);
    }

    @Override
    public List<JobEntity> findByTokenId(Long tokenId) {
        return repository.findByTokenId(tokenId);
    }

    @Override
    public List<JobEntity> findAsyncJobsDueToExecute() {
        final Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        return repository.findAsyncJobsDueToExecute(c.getTime());
    }

    @Override
    public void execute(JobEntity job) {
        if (job instanceof MessageEntity) {
            executeMessageJob(job);
        } else if (job instanceof TimerEntity) {
            executeTimerJob((TimerEntity) job);
        }
    }

    private void executeMessageJob(JobEntity job) {
        executeJobHandler(job);
        delete(job);
    }

    private void executeTimerJob(TimerEntity job) {
        // TODO implement executeTimerJob
    }

    @Override
    public void send(MessageEntity message) {
        final Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.MILLISECOND, getAsyncExecutor().getJobLockTimeMillis());

        message.setDueBy(c.getTime());
        message.setLockExpiresAt(null);
        save(message);

        // TODO set a transaction listener on the job
    }

    @Override
    public void retryAsyncJob(JobEntity job) {
        try {
            Thread.sleep(getAsyncExecutor().getRetryWaitTimeMillis());
        } catch (final InterruptedException e) {
            // Drop through
        }
        getAsyncExecutor().executeAsyncJob(job);
    }

    @Override
    public MessageEntity createMessage() {
        return new MessageEntity();
    }

    @Override
    public JobEntity save(JobEntity job) {
        if (job.getTokenId() != null) {
            final Token token = Context.getCommandContext().getTokenService()
                    .findById(job.getTokenId());
            token.getJobs().add(job);
        }

        final JobEntity savedJob = repository.save(job);

        Context.getCommandContext().getEventDispatcher().dispatchEvent(
                WorkflowEventFactory.createEntityEvent(WorkflowEventType.ENTITY_CREATED, savedJob));
        Context.getCommandContext().getEventDispatcher().dispatchEvent(WorkflowEventFactory
                .createEntityEvent(WorkflowEventType.ENTITY_INITIALIZED, savedJob));

        return savedJob;
    }

    @Override
    public void delete(JobEntity job) {
        if (job.getTokenId() != null) {
            final Token token = Context.getCommandContext().getTokenService()
                    .findById(job.getTokenId());
            token.getJobs().remove(job);
        }
        repository.delete(job);

        Context.getCommandContext().getEventDispatcher().dispatchEvent(
                WorkflowEventFactory.createEntityEvent(WorkflowEventType.ENTITY_DELETED, job));
    }

    private void executeJobHandler(JobEntity job) {
        Token token = null;
        if (job.getTokenId() != null) {
            token = Context.getProcessEngineService().getTokenService().findById(job.getTokenId());
        }
        final Map<String, JobHandler> jobHandlers = Context.getProcessEngineService()
                .getJobHandlers();
        final JobHandler jobHandler = jobHandlers.get(job.getJobHandlerType());
        jobHandler.execute(job, null, token, Context.getCommandContext());
    }

    private AsyncExecutor getAsyncExecutor() {
        return Context.getProcessEngineService().getAsyncExecutor();
    }
}
