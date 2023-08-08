package com.swave.urnr.opensearch.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

import java.util.Date;

@Getter
@Setter
@Document(indexName = "user")
public class UserOpenSearch {

    @Id
    @Field(name = "user_id")
    private Long id;

    @Field(name = "username")
    private String username;

    @Field(name = "provider")
    private String provider;

    @Field(name = "email")
    private String email;
}
