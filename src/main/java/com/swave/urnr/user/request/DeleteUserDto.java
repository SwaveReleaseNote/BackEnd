package com.swave.urnr.user.request;

import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import lombok.*;

@Data
@Builder
public class DeleteUserDto {
@NotNull
    private String code;

    public DeleteUserDto() { }
    public DeleteUserDto(String code) { this.code = code;}
}
