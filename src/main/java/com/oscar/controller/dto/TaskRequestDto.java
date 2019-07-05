package com.oscar.controller.dto;

import com.oscar.controller.model.component.Component;
import com.oscar.controller.model.job.JobType;
import lombok.Data;

import java.util.TreeSet;

@Data
public class TaskRequestDto {

    private Component component;

    private TreeSet<JobType> jobConfig = new TreeSet<>();

    public String isValid() {
        if (component == null) {
            return "Not valid component data!";
        }
        String componentValid = component.isValid();
        if (componentValid != null) {
            return componentValid;
        }
        if (jobConfig == null || jobConfig.size() == 0) {
            return "Not valid pipeline config!";
        }
        return null;
    }
}
