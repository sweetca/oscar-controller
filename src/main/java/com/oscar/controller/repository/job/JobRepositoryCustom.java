package com.oscar.controller.repository.job;

import com.oscar.controller.model.job.Job;
import com.oscar.controller.model.job.JobType;

import java.util.Set;
import java.util.SortedSet;

public interface JobRepositoryCustom {

    SortedSet<Job> findJobs(Set<String> jobIds);

    boolean isFinished(String jobId);

    void lockJob(String jobId);

    void finishJob(String jobId);

    void setTaskId(String taskId, Set<String> jobIds);

    SortedSet<Job> findAllNonLockedNonFinishedJobsByType(JobType type);
}
