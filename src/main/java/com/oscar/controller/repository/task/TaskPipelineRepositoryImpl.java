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
    public boolean findTaskStatusById(String taskId) {
        Query query = Query.query(Criteria.where("_id").is(taskId));
        query.fields().include("finished");
        return this.mongoOperations.findOne(query, TaskPipeline.class).isFinished();
    }

    @Override
    public SortedSet<TaskPipeline> findTaskStatusInProgress() {
        Query query = new Query();
        query.addCriteria(Criteria.where("finished").is(false));
        return new TreeSet<>(this.mongoOperations.find(query, TaskPipeline.class));
    }

    @Override
    public SortedSet<TaskPipeline> findLatestProgress() {
        Query query = new Query();
        query.limit(20);
        return new TreeSet<>(this.mongoOperations.find(query, TaskPipeline.class));
    }

    @Override
    public boolean incrementTaskProgress(String taskId) {
        Query query = Query.query(Criteria.where("_id").is(taskId));
        TaskPipeline task = this.mongoOperations.findOne(query, TaskPipeline.class);
        task.setProgress(task.getProgress() + 1);
        if (task.getProgress() >= task.getJobs().size()) {
            task.setFinished(true);
        }
        this.mongoOperations.updateFirst(
                query,
                Update.update("updated", new Date())
                        .set("progress", task.getProgress())
                        .set("finished", task.isFinished()),
                TaskPipeline.class);
        return task.isFinished();
    }
}
