package com.swave.urnr.chatgpt.config;

import org.springframework.beans.factory.annotation.Value;

public class ChatGPTConfig {

    public static final String AUTHORIZATION = "Authorization";
    public static final String BEARER = "Bearer ";

    @Value("${gpt.api}")
    public static final String API_KEY = null;
    public static final String MODEL = "gpt-3.5-turbo";
    public static final Integer MAX_TOKEN = 3000;
    public static final Double TEMPERATURE = 0.0;
    public static final Double TOP_P = 1.0;
    public static final String MEDIA_TYPE = "application/json; charset=UTF-8";
    public static final String URL = "https://api.openai.com/v1/chat/completions";
    public static final String ROLE_USER = "user";

}