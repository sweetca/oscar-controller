package com.oscar.controller.model.job;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum JobType {

    git_clone(10),

    fossy_scan(20),

    ort_scan(30),

    nvd_update(40);

    private int type;

    JobType(int type) {
        this.type = type;
    }

    public int getType() {
        return this.type;
    }

    @JsonCreator
    public static JobType fromType(int type) {
        for (JobType jobType : JobType.values()) {
            if (jobType.getType() == type) {
                return jobType;
            }
        }
        return null;
    }
}
