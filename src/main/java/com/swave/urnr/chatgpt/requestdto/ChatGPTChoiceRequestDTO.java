package com.swave.urnr.chatgpt.requestdto;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.swave.urnr.util.gpt.Message;
import lombok.*;
import springfox.documentation.annotations.ApiIgnore;

import java.io.Serializable;
@Getter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiIgnore
public class ChatGPTChoiceRequestDTO implements Serializable {

    private Integer index;

    private Message message;

    @JsonProperty("finish_reason")
    private String finishReason;
}