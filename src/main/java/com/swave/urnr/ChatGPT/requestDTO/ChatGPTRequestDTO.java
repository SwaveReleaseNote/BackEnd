package com.swave.urnr.ChatGPT.requestDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ChatGPTRequestDTO implements Serializable {

    private String model;
    private List<Message> messages;

}