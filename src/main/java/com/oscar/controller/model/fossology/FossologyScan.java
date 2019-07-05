package com.oscar.controller.model.fossology;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Document
public class FossologyScan {

    @Id
    private String id;

    @Indexed
    private String component;

    @Indexed
    private String version;

    private Date date = new Date();

    private String originalResponse;

    private ParsedLicenseNode parsedResponse;

    private String error;
}
