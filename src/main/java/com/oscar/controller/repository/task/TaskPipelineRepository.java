package com.oscar.controller.repository.task;

import com.oscar.controller.model.task.TaskPipeline;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TaskPipelineRepository extends MongoRepository<TaskPipeline, String>, TaskPipelineRepositoryCustom {
}
