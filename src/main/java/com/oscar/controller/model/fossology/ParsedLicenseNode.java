package com.oscar.controller.model.fossology;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
public class ParsedLicenseNode {

    private String name;

    private String path;

    private int size = 50;

    private int nbFiles = 0;

    private int nbLevelFiles = 0;

    private int nbLevelDir = 0;

    private Set<String> copyrights = new HashSet<>();

    @JsonProperty("allUniqLicenses")
    private Set<String> uniqueLicenses = new HashSet<>();

    private List<ParsedLicenseNode> children = new ArrayList<>();
}
