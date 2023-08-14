package com.swave.urnr.opensearch.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

@Getter
@Setter
@Document(indexName = "project")
public class ProjectOpenSearch {

    @Id
    @Field(name = "project_id")
    private Long id;

    @Field(name = "project_name")
    private String projectName;

    @Field(name = "description")
    private String description;

    @Field(name = "create_date", type = FieldType.Date)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date createDate;
}

