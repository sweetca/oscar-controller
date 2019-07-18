package com.oscar.controller.model.component;

import com.oscar.controller.model.task.TaskPipeline;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

import static org.apache.commons.lang.StringUtils.isBlank;

@Data
@Document
public class Component {

    @Id
    private String id;

    @Indexed
    private String version;

    private String owner;

    private String name;

    private String url;

    private String branch;

    private ComponentType type;

    private GitCredentials credentials;

    private boolean privateAccess = false;

    private Date date = new Date();

    @Transient
    private TaskPipeline task;

    public String isValid() {
        if (isBlank(url)) {
            return "Not valid component url!";
        }
        if (type == null) {
            return "Not valid component type! Accept github|gitlab|source.";
        }
        if (isBlank(branch)) {
            return "Not valid git branch!";
        }
        if (credentials == null || isBlank(credentials.getAccessToken()) || isBlank(credentials.getAccessToken())) {
            return "Not valid git credentials!";
        }
        return null;
    }
}
