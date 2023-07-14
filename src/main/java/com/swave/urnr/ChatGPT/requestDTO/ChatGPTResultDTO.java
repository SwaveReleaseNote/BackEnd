package com.swave.urnr.ChatGPT.requestDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatGPTResultDTO {
    private String text;
    private Integer index;

    @JsonProperty("finish_reason")
    private String finishReason;

}