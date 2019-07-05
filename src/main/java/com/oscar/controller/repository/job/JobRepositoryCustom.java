package com.oscar.controller.repository.job;

import java.util.Set;

public interface JobRepositoryCustom {
    
    void setTaskId(String taskId, Set<String> jobIds);
}
