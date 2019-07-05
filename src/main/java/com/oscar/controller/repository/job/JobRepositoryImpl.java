package com.oscar.controller.repository.job;

import com.oscar.controller.model.job.Job;
import com.oscar.controller.model.job.JobType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.Date;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public class JobRepositoryImpl implements JobRepositoryCustom {

    private final MongoOperations mongoOperations;

    @Autowired
    public JobRepositoryImpl(MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }

    @Override
    public SortedSet<Job> findJobs(Set<String> jobIds) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").in(jobIds));
        return new TreeSet<>(this.mongoOperations.find(query, Job.class));
    }

    @Override
    public boolean isFinished(String jobId) {
        Query query = new Query();
        query.fields().include("finished");
        query.addCriteria(Criteria.where("_id").is(jobId));
        Job job = this.mongoOperations.findOne(query, Job.class);
        return job.isFinished();
    }

    @Override
    public void lockJob(String jobId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(jobId));
        this.mongoOperations.updateMulti(
                query,
                Update.update("locked", true).set("created", new Date()),
                Job.class);
    }

    @Override
    public void finishJob(String jobId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(jobId));
        this.mongoOperations.updateMulti(
                query,
                Update.update("finished", true).set("created", new Date()),
                Job.class);
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

    @Override
    public SortedSet<Job> findAllNonLockedNonFinishedJobsByType(JobType type) {
        Query query = new Query();
        query
                .addCriteria(Criteria.where("locked").is(false))
                .addCriteria(Criteria.where("finished").is(false))
                .addCriteria(Criteria.where("type").is(type.name()));
        query.with(new Sort(Sort.Direction.DESC, "created"));
        return new TreeSet<>(this.mongoOperations.find(query, Job.class));
    }
}
