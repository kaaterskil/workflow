package com.kaaterskil.workflow.engine.executor;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.kaaterskil.workflow.engine.persistence.entity.JobEntity;

public class AcquiredJobs {

    private final Map<Long, JobEntity> acquiredJobs = new HashMap<>();

    public void addJob(JobEntity job) {
        acquiredJobs.put(job.getId(), job);
    }

    public Collection<JobEntity> getAcquiredJobs() {
        return acquiredJobs.values();
    }

    public boolean contains(Long jobId) {
        return acquiredJobs.containsKey(jobId);
    }

    public int size() {
        return acquiredJobs.size();
    }
}
