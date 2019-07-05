package com.oscar.controller.model.fossology.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CopyrightResultDto {

    private String content;

    private int start;

    private int end;

    private String type;
}
