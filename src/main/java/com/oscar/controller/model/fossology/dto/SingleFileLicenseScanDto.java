package com.oscar.controller.model.fossology.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SingleFileLicenseScanDto {

    private List<MonkResultDto> monk;

    private List<CopyrightResultDto> copyright;

    private List<String> nomos;

    private List<String> ninka;
}