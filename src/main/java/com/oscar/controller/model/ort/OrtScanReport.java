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

    private List<Project> projects = new ArrayList<>();
    private List<Package> packages = new ArrayList<>();
    private List<Scan> scans = new ArrayList<>();

    @Data
    public static class Project {
        private String name;
        private String purl;
        private String file;
        private String homepage;
        private Vcs vcs;
        private List<String> licenses = new ArrayList<>();
        private List<Dependency> dependencies = new ArrayList<>();
    }

    @Data
    public static class Package {
        private String name;
        private String purl;
        private String description;
        private String homepage;
        private String source;
        private Vcs vcs;
        private List<String> licenses = new ArrayList<>();
    }

    @Data
    public static class Scan {
        private String name;
        private String url;
        private Integer fileCount;
        private Integer timeScan;
        private List<String> licenses;
    }

    @Data
    public static class Vcs {
        private String type;
        private String url;
        private String revision;
    }

    @Data
    public static class Dependency {
        private String name;
        private String scope;
        private List<Dependency> dependencies = new ArrayList<>();
    }
}


