package com.oscar.controller.api;

import com.oscar.controller.model.job.Job;
import com.oscar.controller.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

@RestController
public class JobController {

    private final JobService jobService;

    @Autowired
    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @GetMapping(value = "/find_job/{type}/{podName}")
    public ResponseEntity<Job> findJob(@NotNull @PathVariable("type") Integer type,
                                       @NotNull @PathVariable("podName") String podName) {

        Job job = this.jobService.findJobForProcessing(type, podName);
        if (job != null) {
            return new ResponseEntity<>(job, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(value = "/finish_job/{jobId}/{podName}")
    public ResponseEntity<Job> findJob(@NotNull @PathVariable("jobId") String jobId,
                                       @NotNull @PathVariable("podName") String podName) {
        return ResponseEntity.ok(this.jobService.finishJob(jobId, podName));
    }
}
