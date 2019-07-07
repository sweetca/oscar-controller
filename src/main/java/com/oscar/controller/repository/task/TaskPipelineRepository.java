package com.oscar.controller.repository.task;

import com.oscar.controller.model.task.TaskPipeline;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface TaskPipelineRepository extends MongoRepository<TaskPipeline, String>, TaskPipelineRepositoryCustom {

    Optional<TaskPipeline> findByComponent(String component);

    Optional<TaskPipeline> findByComponentAndVersion(String component, String version);
}
