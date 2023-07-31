package com.swave.urnr.chatgpt.requestdto;

import com.swave.urnr.util.gpt.Message;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import springfox.documentation.annotations.ApiIgnore;

import java.io.Serializable;
import java.util.List;

@Getter
@ApiIgnore
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ChatGPTHttpRequestRequestDTO implements Serializable {

    private String model;
    private List<Message> messages;

}