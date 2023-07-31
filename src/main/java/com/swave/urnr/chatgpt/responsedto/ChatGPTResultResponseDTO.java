package com.swave.urnr.chatgpt.responsedto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import springfox.documentation.annotations.ApiIgnore;

@Data
@Builder
@ApiIgnore
@NoArgsConstructor
@AllArgsConstructor
public class ChatGPTResultResponseDTO {
    private String text;
    private Integer index;

    @JsonProperty("finish_reason")
    private String finishReason;

}