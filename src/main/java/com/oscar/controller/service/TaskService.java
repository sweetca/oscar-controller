package com.oscar.controller.service;

import com.oscar.controller.exceptions.OscarDataException;
import com.oscar.controller.model.task.TaskPipeline;
import com.oscar.controller.repository.job.JobRepository;
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

    private final JobRepository jobRepository;

    @Autowired
    public TaskService(TaskPipelineRepository taskPipelineRepository,
                       JobRepository jobRepository) {
        this.taskPipelineRepository = taskPipelineRepository;
        this.jobRepository = jobRepository;
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
}
