package com.oscar.controller.repository.task;

import com.oscar.controller.model.task.TaskPipeline;

import java.util.SortedSet;

public interface TaskPipelineRepositoryCustom {

    TaskPipeline findTaskStatusById(String taskId);

    SortedSet<TaskPipeline> findTaskStatusInProgress();

    SortedSet<TaskPipeline> findLatestProgress();

    boolean incrementTaskProgress(String taskId);
}
