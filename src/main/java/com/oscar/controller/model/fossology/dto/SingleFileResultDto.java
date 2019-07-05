package com.oscar.controller.model.fossology.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SingleFileResultDto {

    private String file;

    private SingleFileLicenseScanDto output;
}
