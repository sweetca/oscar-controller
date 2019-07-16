package com.oscar.controller.service;

import com.oscar.controller.dto.TaskRequestDto;
import com.oscar.controller.exceptions.OscarDataException;
import com.oscar.controller.model.component.Component;
import com.oscar.controller.model.job.Job;
import com.oscar.controller.model.job.JobType;
import com.oscar.controller.model.task.TaskPipeline;
import com.oscar.controller.repository.component.ComponentRepository;
import com.oscar.controller.repository.task.TaskPipelineRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.SortedSet;
import java.util.TreeSet;

@Service
@Slf4j
public class TaskService {

    private final TaskPipelineRepository taskPipelineRepository;
    private final JobService jobService;
    private final ComponentRepository componentRepository;

    @Autowired
    public TaskService(TaskPipelineRepository taskPipelineRepository,
                       JobService jobService,
                       ComponentRepository componentRepository) {
        this.taskPipelineRepository = taskPipelineRepository;
        this.jobService = jobService;
        this.componentRepository = componentRepository;
    }

    public TaskPipeline findFullTaskById(String taskId) {
        TaskPipeline task = this.taskPipelineRepository
                .findById(taskId)
                .orElseThrow(OscarDataException::noTaskFound);
        task.getJobsReference()
                .addAll(this.jobService.findJobsByIds(task.getJobs()));
        return task;
    }

    public Optional<TaskPipeline> findFullTaskByComponent(String component) {
        TaskPipeline task = this.taskPipelineRepository.findByComponent(component).orElse(null);
        if (task != null) {
            task.getJobsReference().addAll(this.jobService.findJobsByIds(task.getJobs()));
            return Optional.of(task);
        }
        return Optional.empty();
    }

    public Optional<TaskPipeline> findFullTaskByComponentAndVersion(String component, String version) {
        TaskPipeline task = this.taskPipelineRepository.findByComponentAndVersion(component, version).orElse(null);
        if (task != null) {
            task.getJobsReference().addAll(this.jobService.findJobsByIds(task.getJobs()));
            return Optional.of(task);
        }
        return Optional.empty();
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

    public SortedSet<TaskPipeline> findLatestProgress() {
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
        if (jobConfig.contains(JobType.vulnerabilities)) {
            jobConfig.add(JobType.ort_analyze);
        }
        if (jobConfig.contains(JobType.ort_scan)) {
            jobConfig.remove(JobType.ort_analyze);
        }

        SortedSet<Job> jobs = this.jobService.setupJobsFromRequest(component, jobConfig);

        TaskPipeline taskPipeline = new TaskPipeline();
        taskPipeline.setComponent(component.getId());
        taskPipeline.setVersion(component.getVersion());
        taskPipeline.setJobsReference(jobs);

        taskPipeline = this.taskPipelineRepository.save(taskPipeline);
        String taskId = taskPipeline.getId();
        taskPipeline.getJobsReference().forEach(j -> j.setTask(taskId));
        this.jobService.setTaskId(taskId, taskPipeline.getJobs());

        return taskPipeline;
    }
}
