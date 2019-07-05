package com.oscar.controller.repository.job;

import com.oscar.controller.model.job.Job;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.Set;

public class JobRepositoryImpl implements JobRepositoryCustom {

    private final MongoOperations mongoOperations;

    @Autowired
    public JobRepositoryImpl(MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }

    @Override
    public void setTaskId(String taskId, Set<String> jobIds) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").in(jobIds));
        this.mongoOperations.updateMulti(
                query,
                Update.update("task", taskId),
                Job.class);
    }
}
