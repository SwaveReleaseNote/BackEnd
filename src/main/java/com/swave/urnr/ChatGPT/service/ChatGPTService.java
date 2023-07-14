package com.swave.urnr.ChatGPT.service;


import com.swave.urnr.ChatGPT.config.ChatGPTConfig;
import com.swave.urnr.ChatGPT.requestDTO.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import java.util.Collections;

@Service
public class ChatGPTService {

    private static RestTemplate restTemplate = new RestTemplate();

    //    @Value("${secret.key}")
//    private String API_KEY;
    public HttpEntity<ChatGPTRequestDTO> buildHttpEntity(ChatGPTRequestDTO requestDto) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(ChatGPTConfig.MEDIA_TYPE));
        headers.add(ChatGPTConfig.AUTHORIZATION, ChatGPTConfig.BEARER + ChatGPTConfig.API_KEY);
        return new HttpEntity<>(requestDto, headers);
    }

    public ChatGPTChoice getResponse(HttpEntity<ChatGPTRequestDTO> chatGptRequestDtoHttpEntity) {
        ResponseEntity<ChatGPTResponseDTO> responseEntity = restTemplate.postForEntity(
                ChatGPTConfig.URL,
                chatGptRequestDtoHttpEntity,
                ChatGPTResponseDTO.class);
        return responseEntity.getBody().getChoices().get(0);
    }

    public ChatGPTChoice askQuestion(ChatGPTQuestionRequestDTO requestDto) {
        Message message = Message.builder()
                .role(ChatGPTConfig.ROLE_USER)
                .content(requestDto.getQuestion())
                .build();
        System.out.println(message);
        return this.getResponse(
                this.buildHttpEntity(
                        new ChatGPTRequestDTO(
                                ChatGPTConfig.MODEL,
                                Collections.singletonList(message)
                        )
                )
        );
    }

    public ChatGPTResultDTO chatGptResult(ChatGPTQuestionRequestDTO requestDto) {

        ChatGPTChoice chatGptChoice=askQuestion(requestDto);
        ChatGPTResultDTO chatResultDto = ChatGPTResultDTO.builder()
                .index(chatGptChoice.getIndex())
                .text(chatGptChoice.getMessage().getContent())
                .finishReason(chatGptChoice.getFinishReason())
                .build();
        return chatResultDto;
    }
}