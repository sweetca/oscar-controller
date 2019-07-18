package com.oscar.controller.model.component;

import com.oscar.controller.dto.VulnerabilityDto;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Data
@Document
public class ComponentNvd {

    @Id
    private String id;

    @Indexed
    private String component;

    @Indexed
    private String version;

    Map<String, Set<VulnerabilityDto>> nvd = new HashMap<>();
}
