package com.oscar.controller.service;

import com.oscar.controller.config.AppConfig;
import com.oscar.controller.exceptions.OscarDataException;
import com.oscar.controller.model.component.Component;
import com.oscar.controller.model.job.Job;
import com.oscar.controller.model.job.JobType;
import com.oscar.controller.repository.job.JobRepository;
import com.oscar.controller.repository.task.TaskPipelineRepository;
import com.oscar.controller.util.UrlUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.Paths;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import static org.apache.commons.lang.StringUtils.isBlank;

@Service
@Slf4j
public class JobService {

    private final AppConfig appConfig;
    private final JobRepository jobRepository;
    private final TaskPipelineRepository taskPipelineRepository;

    @Autowired
    public JobService(AppConfig appConfig,
                      JobRepository jobRepository,
                      TaskPipelineRepository taskPipelineRepository) {
        this.appConfig = appConfig;
        this.jobRepository = jobRepository;
        this.taskPipelineRepository = taskPipelineRepository;
    }

    public SortedSet<Job> findJobsByIds(final Set<String> ids) {
        return this.jobRepository.findJobs(ids);
    }

    public void setTaskId(final String taskId, final Set<String> jobIds) {
        this.jobRepository.setTaskId(taskId, jobIds);
    }

    public synchronized Job findJobForProcessing(final Integer type, final String podName) {
        JobType jType = JobType.fromType(type);
        SortedSet<Job> availableJobs = this.jobRepository.findAllNonLockedNonFinishedJobsByType(jType);

        for (Job job : availableJobs) {
            if (isBlank(job.getRelatedJob()) || this.jobRepository.isFinished(job.getRelatedJob())) {
                this.jobRepository.lockJob(job.getId());
                log.info("Providing job {} on find request of pod {}", job.getId(), podName);
                return job;
            }
        }
        log.debug("No job of type {} for pod {}", type, podName);
        return null;
    }

    public Job finishJob(final String jobId, final String podName) {
        Job job = this.jobRepository.findById(jobId).orElseThrow(OscarDataException::noJobFound);
        this.jobRepository.finishJob(job.getId());
        this.taskPipelineRepository.incrementTaskProgress(job.getTask());
        log.debug("Job finished {} by pod {}", jobId, podName);
        return job;
    }


    public SortedSet<Job> setupJobsFromRequest(final Component component, final SortedSet<JobType> jobConfig) {
        SortedSet<Job> result = new TreeSet<>();
        String relatedJob = null;
        for (JobType type : jobConfig) {
            Job job = new Job();
            job.setType(type);
            job.setComponent(component.getId());

            Map<String, Object> payload = job.getPayload();
            payload.put("gitId", component.getId());
            payload.put("componentType", component.getType().name());
            payload.put("accessToken", component.getCredentials().getAccessToken());
            payload.put("userName", component.getCredentials().getAccessToken());
            payload.put("componentPath", getRepoPath(component.getType().name(), component.getId()));
            payload.put("gitBranch", component.getBranch());

            if (component.isPrivateAccess()) {
                payload.put("gitUrl", UrlUtil.privateAccessUrl(component));
            } else {
                payload.put("gitUrl", component.getUrl());
            }

            job.setRelatedJob(relatedJob);
            job = this.jobRepository.save(job);
            relatedJob = job.getId();
            result.add(job);
        }
        return result;
    }

    private String getRepoPath(String type, String id) {
        return Paths.get(this.appConfig.getRepositoryDir(), type, id).normalize().toString();
    }
}
