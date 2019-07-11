package com.oscar.controller.model.ort;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Document
public class OrtScan {

    @Id
    private String id;

    @Indexed
    private String component;

    @Indexed
    private String version;

    private Date date = new Date();

    private String error;

    @Transient
    private OrtScanReport report;
}
