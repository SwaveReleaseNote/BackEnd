package com.swave.urnr.chatgpt.service;


import com.swave.urnr.util.gpt.Message;
import com.swave.urnr.chatgpt.config.ChatGPTConfig;
import com.swave.urnr.chatgpt.requestdto.*;
import com.swave.urnr.chatgpt.responsedto.ChatGPTResultResponseDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import java.util.Collections;

@Service
public class ChatGPTService {

    private static RestTemplate restTemplate = new RestTemplate();

    //    @Value("${secret.key}")
//    private String API_KEY;
    public HttpEntity<ChatGPTHttpRequestRequestDTO> buildHttpEntity(ChatGPTHttpRequestRequestDTO requestDto) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(ChatGPTConfig.MEDIA_TYPE));
        headers.add(ChatGPTConfig.AUTHORIZATION, ChatGPTConfig.BEARER + ChatGPTConfig.API_KEY);
        return new HttpEntity<>(requestDto, headers);
    }

    public ChatGPTChoiceRequestDTO getResponse(HttpEntity<ChatGPTHttpRequestRequestDTO> chatGptRequestDtoHttpEntity) {
        ResponseEntity<ChatGPTHttpResponseRequestDTO> responseEntity = restTemplate.postForEntity(
                ChatGPTConfig.URL,
                chatGptRequestDtoHttpEntity,
                ChatGPTHttpResponseRequestDTO.class);
        return responseEntity.getBody().getChoices().get(0);
    }

    public ChatGPTChoiceRequestDTO askQuestion(ChatGPTQuestionRequestDTO requestDto) {
        Message message = Message.builder()
                .role(ChatGPTConfig.ROLE_USER)
                .content(requestDto.getQuestion())
                .build();
        System.out.println(message);
        return this.getResponse(
                this.buildHttpEntity(
                        new ChatGPTHttpRequestRequestDTO(
                                ChatGPTConfig.MODEL,
                                Collections.singletonList(message)
                        )
                )
        );
    }

    public ChatGPTResultResponseDTO chatGptResult(ChatGPTQuestionRequestDTO requestDto) {

        ChatGPTChoiceRequestDTO chatGptChoiceRequestDTO =askQuestion(requestDto);
        ChatGPTResultResponseDTO chatResultDto = ChatGPTResultResponseDTO.builder()
                .index(chatGptChoiceRequestDTO.getIndex())
                .text(chatGptChoiceRequestDTO.getMessage().getContent())
                .finishReason(chatGptChoiceRequestDTO.getFinishReason())
                .build();
        return chatResultDto;
    }
}