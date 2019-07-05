package com.oscar.controller.model.component;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.Date;

import static org.apache.commons.lang.StringUtils.isBlank;

@Data
public class Component {

    @Id
    private String id;

    private String owner;

    private String name;

    private String url;

    private String branch;

    private ComponentType type;

    private GitCredentials credentials;

    private boolean privateAccess = false;

    private String version;

    private Date date = new Date();

    public String isValid() {
        if (isBlank(url)) {
            return "Not valid component url!";
        }
        if (type == null) {
            return "Not valid component type! Accept github|gitlab|source.";
        }
        if (credentials == null || isBlank(credentials.getAccessToken()) || isBlank(credentials.getAccessToken())) {
            return "Not valid git credentials!";
        }
        return null;
    }
}
