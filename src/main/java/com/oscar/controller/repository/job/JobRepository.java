package com.oscar.controller.repository.job;

import com.oscar.controller.model.job.Job;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface JobRepository extends MongoRepository<Job, String>, JobRepositoryCustom {
}
