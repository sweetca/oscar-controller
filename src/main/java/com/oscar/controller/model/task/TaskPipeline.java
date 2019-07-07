package com.oscar.controller.model.task;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.oscar.controller.model.job.Job;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.*;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Document
public class TaskPipeline implements Comparable<TaskPipeline> {

    @Id
    private String id;

    @Indexed
    private String component;

    @Indexed
    private String version;

    @Indexed
    private boolean finished = false;

    private long progress = 0L;

    private Date created = new Date();

    private Date updated = new Date();

    private Set<String> jobs = new HashSet<>();

    @Transient
    private SortedSet<Job> jobsReference = new TreeSet<>();

    public void setJobsReference(SortedSet<Job> jobs) {
        if (jobs != null) {
            this.jobsReference = jobs;
            this.jobs.addAll(this.jobsReference.stream().map(Job::getId).collect(Collectors.toSet()));
        }
    }

    @Override
    public int compareTo(TaskPipeline o) {
        return this.created.compareTo(o.getCreated());
    }

    public int getProgressBar() {
        if (jobs.size() == progress) {
            return 100;
        } else {
            return (100 / jobs.size()) * (int) progress;
        }
    }
}
