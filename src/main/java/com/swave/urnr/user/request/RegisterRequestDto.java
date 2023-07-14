package com.swave.urnr.user.request;
import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import lombok.*;

/*
TODO: Find how to implement NOTNULL VALIDATE without hardcoding, also need to find Email validation system with RNG

 */
@Data
@Builder
public class RegisterRequestDto {
    @NotNull
    private String email;
    @NotNull
    private String password;
    @NotNull
    private String name;
}

