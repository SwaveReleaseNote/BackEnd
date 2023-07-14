package com.swave.urnr.ChatGPT.controller;

import com.swave.urnr.ChatGPT.requestDTO.ChatGPTQuestionRequestDTO;
import com.swave.urnr.ChatGPT.requestDTO.ChatGPTResultDTO;
import com.swave.urnr.ChatGPT.service.ChatGPTService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/gpt/")
public class ChatGptController {

    private final ChatGPTService chatGPTService;

    public ChatGptController(ChatGPTService chatGPTService) {
        this.chatGPTService = chatGPTService;
    }

    @PostMapping("/question")
    public ChatGPTResultDTO sendQuestion(@RequestBody ChatGPTQuestionRequestDTO chatGPTQuestionRequestDTO) {
        return chatGPTService.chatGptResult(chatGPTQuestionRequestDTO);
    }
}