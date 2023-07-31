package com.swave.urnr.chatgpt.requestdto;

import lombok.*;
import springfox.documentation.annotations.ApiIgnore;

import java.io.Serializable;
import java.util.List;

@Data
@Getter
@NoArgsConstructor
@Builder
@ApiIgnore
@AllArgsConstructor
public class ChatGPTHttpResponseRequestDTO implements Serializable {

    private List<ChatGPTChoiceRequestDTO> choices;
}