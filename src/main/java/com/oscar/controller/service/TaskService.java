package com.oscar.controller.service;

import com.oscar.controller.config.AppConfig;
import com.oscar.controller.dto.TaskRequestDto;
import com.oscar.controller.exceptions.OscarDataException;
import com.oscar.controller.model.component.Component;
import com.oscar.controller.model.job.Job;
import com.oscar.controller.model.job.JobType;
import com.oscar.controller.model.task.TaskPipeline;
import com.oscar.controller.repository.component.ComponentRepository;
import com.oscar.controller.repository.job.JobRepository;
import com.oscar.controller.repository.task.TaskPipelineRepository;
import com.oscar.controller.util.UrlUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.Paths;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

@Service
@Slf4j
public class TaskService {

    private final AppConfig appConfig;
    private final TaskPipelineRepository taskPipelineRepository;
    private final JobRepository jobRepository;
    private final ComponentRepository componentRepository;

    @Autowired
    public TaskService(AppConfig appConfig,
                       TaskPipelineRepository taskPipelineRepository,
                       JobRepository jobRepository,
                       ComponentRepository componentRepository) {
        this.appConfig = appConfig;
        this.taskPipelineRepository = taskPipelineRepository;
        this.jobRepository = jobRepository;
        this.componentRepository = componentRepository;
    }

    public TaskPipeline findFullTaskById(String taskId) {
        TaskPipeline task = this.taskPipelineRepository
                .findById(taskId)
                .orElseThrow(OscarDataException::noTaskFound);
        task.getJobs()
                .forEach(jobId -> task.getJobsReference()
                        .add(this.jobRepository.findById(jobId)
                                .orElseThrow(OscarDataException::noJobFound)));
        return task;
    }

    public TaskPipeline findStatusTaskById(String taskId) {
        TaskPipeline task = this.taskPipelineRepository.findTaskStatusById(taskId);
        if (task == null) {
            throw OscarDataException.noTaskFound();
        }
        return task;
    }

    public SortedSet<TaskPipeline> findTaskStatusInProgress() {
        return this.taskPipelineRepository.findTaskStatusInProgress();
    }

    public TaskPipeline setupTask(TaskRequestDto request) {
        String taskValid = request.isValid();
        if (taskValid != null) {
            throw new OscarDataException(taskValid);
        }

        Component component = request.getComponent();
        component.setVersion(System.currentTimeMillis() + "");
        component = this.componentRepository.save(component);

        TreeSet<JobType> jobConfig = request.getJobConfig();
        if (!jobConfig.contains(JobType.git_clone)) {
            jobConfig.add(JobType.git_clone);
        }

        SortedSet<Job> jobs = this.setupJobsFromRequest(component, jobConfig);

        TaskPipeline taskPipeline = new TaskPipeline();
        taskPipeline.setComponent(component.getId());
        taskPipeline.setJobsReference(jobs);

        taskPipeline = this.taskPipelineRepository.save(taskPipeline);
        this.jobRepository.setTaskId(taskPipeline.getId(), taskPipeline.getJobs());

        return taskPipeline;
    }

    private SortedSet<Job> setupJobsFromRequest(Component component, SortedSet<JobType> jobConfig) {
        SortedSet<Job> result = new TreeSet<>();
        jobConfig.forEach(type -> {
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

            job = this.jobRepository.save(job);
            result.add(job);
        });
        return result;
    }

    private String getRepoPath(String type, String id) {
        return Paths.get(this.appConfig.getRepositoryDir(), type, id).normalize().toString();
    }
}
