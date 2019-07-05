package com.oscar.controller.model.fossology.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Data;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = NoMatchMonkResultDto.class, name = "no-match"),
        @JsonSubTypes.Type(value = FullMatchMonkResultDto.class, name = "full"),
        @JsonSubTypes.Type(value = DiffMatchMonkResultDto.class, name = "diff")
})

abstract class MonkResultDto {
}

@JsonTypeName("no-match")
class NoMatchMonkResultDto extends MonkResultDto {
}

@Data
@JsonTypeName("full")
class FullMatchMonkResultDto extends MonkResultDto {

    private String license;

    @JsonProperty("ref-pk")
    private int refPk;

    private String matched;
}

@Data
@JsonTypeName("diff")
class DiffMatchMonkResultDto extends MonkResultDto {

    private String license;

    @JsonProperty("ref-pk")
    private int refPk;

    private int rank;

    private String diffs;
}
