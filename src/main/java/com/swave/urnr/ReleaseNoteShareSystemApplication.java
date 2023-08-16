package com.swave.urnr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.filter.CharacterEncodingFilter;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
public class ReleaseNoteShareSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReleaseNoteShareSystemApplication.class, args);
    }

}
