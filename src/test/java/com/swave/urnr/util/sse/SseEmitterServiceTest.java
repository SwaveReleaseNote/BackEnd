package com.swave.urnr.util.sse;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.swave.urnr.user.domain.User;
import com.swave.urnr.user.repository.UserRepository;
import com.swave.urnr.user.requestdto.UserRegisterRequestDTO;
import com.swave.urnr.user.responsedto.UserEntityResponseDTO;
import com.swave.urnr.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.transaction.Transactional;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
@RequiredArgsConstructor
public class SseEmitterServiceTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SSEEmitterService sseEmitterService;
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebTestClient webTestClient;

    @Test
    @DisplayName("Emitter 배포 테스트")
    @Transactional
    void subscribeEmitter() {
        SseEmitter sseEmitter = sseEmitterService.subscribeEmitter("1", "Hello, world!");

        try{
            MvcResult mvcResult = mockMvc.perform(
                            MockMvcRequestBuilders.get("/api/sse/emitter/1/message/test")
                                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andReturn();

            // Extract SSE messages from the MvcResult
            String content = mvcResult.getResponse().getContentAsString();
            String[] lines = content.split("\n");
            JSONObject jsonObject = new JSONObject(lines[0].substring(5));
            log.info(lines[0]);
            // JWT token value
            assertEquals(jsonObject.getString("data"), "test");
            assertEquals(jsonObject.getString("type"), "TOKEN");
        }catch(Exception e){
            e.printStackTrace();
        }
    }


    @Test
    @DisplayName("Emitter 메세지 발신 테스트")
    @Transactional
    void sendMessageToEmitter() {
        /*
        TODO
         */
    }

    

}
