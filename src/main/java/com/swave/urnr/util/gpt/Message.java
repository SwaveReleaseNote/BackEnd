package com.swave.urnr.util.gpt;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import springfox.documentation.annotations.ApiIgnore;

import java.io.Serializable;

@Data
@Builder
@ApiIgnore
@JsonSerialize(using = MessageSerializer.class)
@NoArgsConstructor
public class Message implements Serializable {
    private String role;
    private String content;
    @JsonCreator
    public Message(@JsonProperty("role") String role, @JsonProperty("content") String content) {
        this.role=role;
        this.content=content;
    }


}