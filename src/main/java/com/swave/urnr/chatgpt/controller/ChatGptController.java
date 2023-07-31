package com.swave.urnr.chatgpt.controller;

import com.swave.urnr.chatgpt.requestdto.ChatGPTQuestionRequestDTO;
import com.swave.urnr.chatgpt.responsedto.ChatGPTResultResponseDTO;
import com.swave.urnr.chatgpt.service.ChatGPTService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*", allowCredentials = "true")
public class ChatGptController {
    private final ChatGPTService chatGPTService;

    @PostMapping("/api/gpt/question")
    public ChatGPTResultResponseDTO sendQuestion(@RequestBody ChatGPTQuestionRequestDTO chatGPTQuestionRequestDTO) {
        return chatGPTService.chatGptResult(chatGPTQuestionRequestDTO);
    }
}