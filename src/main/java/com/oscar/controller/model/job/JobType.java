package com.oscar.controller.model.job;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum JobType {

    git_clone(10),

    fossy_scan(20),

    ort_analyze(30),

    ort_scan(31),

    nvd_update(40),

    vulnerabilities(41);

    private int type;

    JobType(int type) {
        this.type = type;
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

    public int getType() {
        return this.type;
    }
}
