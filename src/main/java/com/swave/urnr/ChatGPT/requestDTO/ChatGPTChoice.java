package com.swave.urnr.ChatGPT.requestDTO;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;
@Getter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatGPTChoice implements Serializable {

    private Integer index;

    private Message message;

    @JsonProperty("finish_reason")
    private String finishReason;
}