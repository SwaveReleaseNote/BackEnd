package com.swave.urnr.chatgpt.requestdto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import springfox.documentation.annotations.ApiIgnore;

import java.io.Serializable;
@Getter
@ApiIgnore
@AllArgsConstructor
@NoArgsConstructor
public class ChatGPTQuestionRequestDTO implements Serializable {
    //    private String question;
    private String question;
}