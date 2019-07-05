package com.oscar.controller.model.fossology.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ScanResultDto {

    private String error;

    @JsonProperty("main-license")
    private SingleFileLicenseScanDto mainLicense;

    @JsonProperty("file-licenses")
    private List<SingleFileResultDto> fileLicenses;
}
