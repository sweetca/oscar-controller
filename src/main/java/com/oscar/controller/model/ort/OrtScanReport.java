package com.oscar.controller.model.ort;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Document
public class OrtScanReport {

    @Id
    private String id;

    @Indexed
    private String component;

    @Indexed
    private String version;

    private Date date = new Date();

    private List<ScanComponent> result;
    private String type;

    @Data
    public static class ScanComponent {
        private String name;
        private String path;
        private String purl;
        private List<String> licenses = new ArrayList<>();
        private List<ScanPackage> packages = new ArrayList<>();
    }

    @Data
    public static class ScanPackage {
        private String name;
        private String purl;
        private List<String> parents = new ArrayList<>();
        private List<String> scopes = new ArrayList<>();
        private List<String> licenses = new ArrayList<>();
    }
}
