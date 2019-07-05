package com.oscar.controller.dto;

import com.oscar.controller.model.component.Component;
import com.oscar.controller.model.component.GitCredentials;
import com.oscar.controller.model.job.JobType;
import lombok.Data;

import java.util.TreeSet;

@Data
public class TaskRequestDto {

    private Component component;

    private GitCredentials credentials;

    private TreeSet<JobType> jobConfig = new TreeSet<>();
}
