package com.swave.urnr.opensearch.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.swave.urnr.util.type.UserRole;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

import java.util.Date;

@Getter
@Setter
@Document(indexName = "user_in_project")
public class UserInProjectOpenSearch {

    @Id
    @Field(name = "user_in_project_id")
    private Long id;

    @Field(name = "user_id")
    private Long user_id;

    @Field(name = "project_id")
    private Long project_id;

    @Field(name = "role")
    private Long role;

}
