package com.oscar.controller.model.ort;

import lombok.Data;

@Data
public class OrtScanError {

    private String component;

    private String version;

    private String error;
}
