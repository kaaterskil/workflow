package com.kaaterskil.workflow.engine.service;

import java.util.List;

import com.kaaterskil.workflow.engine.persistence.entity.JobEntity;
import com.kaaterskil.workflow.engine.persistence.entity.MessageEntity;

public interface JobService {

    JobEntity findById(Long jobId);

    List<JobEntity> findByTokenId(Long tokenId);

    List<JobEntity> findAsyncJobsDueToExecute();

    void execute(JobEntity job);

    void send(MessageEntity message);

    void retryAsyncJob(JobEntity job);

    MessageEntity createMessage();

    JobEntity save(JobEntity job);

    void delete(JobEntity job);
}
