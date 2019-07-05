package com.oscar.controller.model.component;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class Component {

    @Id
    private String id;

    private String owner;

    private String name;

    private String url;

    private ComponentType type;

    private GitCredentials credentials;

    private Long version;
}
