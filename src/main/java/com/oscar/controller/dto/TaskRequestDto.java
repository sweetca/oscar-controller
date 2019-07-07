package com.oscar.controller.dto;

import com.oscar.controller.model.component.Component;
import com.oscar.controller.model.job.JobType;
import lombok.Data;

import java.util.TreeSet;
import java.util.stream.Collectors;

@Data
public class TaskRequestDto {

    private Component component;

    private TreeSet<Integer> types = new TreeSet<>();

    public TreeSet<JobType> getJobConfig() {
        return types
                .stream()
                .map(JobType::fromType)
                .collect(Collectors.toCollection(TreeSet::new));
    }

    public String isValid() {
        if (component == null) {
            return "Not valid component data!";
        }
        String componentValid = component.isValid();
        if (componentValid != null) {
            return componentValid;
        }
        if (types == null || types.size() == 0) {
            return "Not valid pipeline config!";
        }
        return null;
    }
}
