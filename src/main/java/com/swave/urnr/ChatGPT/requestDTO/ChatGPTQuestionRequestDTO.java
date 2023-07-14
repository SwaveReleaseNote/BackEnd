package com.swave.urnr.ChatGPT.requestDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;
@Getter
@AllArgsConstructor
public class ChatGPTQuestionRequestDTO implements Serializable {
    //    private String question;
    private String question;
}