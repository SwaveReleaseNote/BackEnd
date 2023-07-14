package com.swave.urnr.ChatGPT.requestDTO;

import lombok.*;

import java.io.Serializable;
import java.util.List;

@Data
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ChatGPTResponseDTO implements Serializable {

    private List<ChatGPTChoice> choices;

}