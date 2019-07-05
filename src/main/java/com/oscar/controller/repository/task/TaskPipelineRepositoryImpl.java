package com.oscar.controller.repository.task;

import com.oscar.controller.model.task.TaskPipeline;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.Date;
import java.util.SortedSet;
import java.util.TreeSet;

public class TaskPipelineRepositoryImpl implements TaskPipelineRepositoryCustom {

    private final MongoOperations mongoOperations;

    @Autowired
    public TaskPipelineRepositoryImpl(MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }

    @Override
    public TaskPipeline findTaskStatusById(String taskId) {
        Query query = new Query();
        query.fields().include("_id");
        query.fields().include("progress");
        query.fields().include("finished");
        query.fields().include("created");
        query.fields().include("updated");
        return this.mongoOperations.findOne(query, TaskPipeline.class);
    }

    @Override
    public SortedSet<TaskPipeline> findTaskStatusInProgress() {
        Query query = new Query();
        query.addCriteria(Criteria.where("finished").is(false));
        return new TreeSet<>(this.mongoOperations.find(query, TaskPipeline.class));
    }

    @Override
    public void incrementTaskProgress(String taskId) {
        Query query = new Query(Criteria.where("_id").is(taskId));
        this.mongoOperations.updateFirst(
                query,
                Update.update("updated", new Date()).inc("progress", 1),
                TaskPipeline.class);
    }
}
