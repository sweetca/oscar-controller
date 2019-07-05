package com.oscar.controller.model.job;

import lombok.Data;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Data
@Document
public class Job implements Comparable<Job> {

    @Id
    private String id;

    @Indexed
    private JobType type;

    @Indexed
    private String component;

    @Indexed
    private String task;

    @Indexed
    private String relatedJob;

    @Indexed
    private boolean finished = false;

    @Indexed
    private boolean locked = false;

    private Date created = new Date();

    private Date updated = new Date();

    private Map<String, Object> payload = new HashMap<>();

    @Override
    public int compareTo(Job o) {
        return this.created.compareTo(o.getCreated());
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(this.getCreated().getTime())
                .append(this.getType())
                .hashCode();
    }
}
