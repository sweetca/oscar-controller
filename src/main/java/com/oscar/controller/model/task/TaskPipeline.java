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

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Document
public class TaskPipeline {

    @Id
    private String id;

    private int progress = 0;

    @Indexed
    private boolean finished = false;

    private Date created = new Date();

    private Date updated = new Date();

    private Set<String> jobs = new HashSet<>();

    @Transient
    private SortedSet<Job> jobsReference = new TreeSet<>();
}
