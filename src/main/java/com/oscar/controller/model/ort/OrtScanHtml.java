package com.oscar.controller.model.ort;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Document
public class OrtScanHtml {

    @Id
    private String id;

    @Indexed
    private String component;

    @Indexed
    private String version;

    private Date date = new Date();

    private String content;
}
