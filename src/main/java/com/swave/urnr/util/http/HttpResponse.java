package com.swave.urnr.util.http;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class HttpResponse {
    private String message;
    private String description;

    @Builder
    public HttpResponse(String message, String description){
        this.message = message;
        this.description = description;
    }

}
