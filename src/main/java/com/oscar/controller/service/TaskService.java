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

        SortedSet<Job> jobs = this.jobService.setupJobsFromRequest(component, jobConfig);

        TaskPipeline taskPipeline = new TaskPipeline();
        taskPipeline.setComponent(component.getId());
        taskPipeline.setJobsReference(jobs);

        taskPipeline = this.taskPipelineRepository.save(taskPipeline);
        String taskId = taskPipeline.getId();
        taskPipeline.getJobsReference().forEach(j -> j.setTask(taskId));
        this.jobService.setTaskId(taskId, taskPipeline.getJobs());

        return taskPipeline;
    }
}
