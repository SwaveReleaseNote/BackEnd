package com.swave.urnr.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swave.urnr.user.domain.User;
import com.swave.urnr.user.repository.UserRepository;
import com.swave.urnr.user.requestdto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete; ;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
@RequiredArgsConstructor
class UserControllerTest {

    @Autowired
    private MockMvc mockmvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;


    @BeforeEach
    @DisplayName("사용자 생성 테스트")
    void setAccountToServer() throws Exception {
        userRepository.deleteAll();

        UserRegisterRequestDTO userRegisterRequestDto = new UserRegisterRequestDTO("corgiwalke@gmail.com","1q2w3e4r","전강훈");

        String json = objectMapper.writeValueAsString(userRegisterRequestDto);
        log.info(json);
        MvcResult result = mockmvc.perform(post("/api/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().is(201))
                .andReturn();


        User user = User.builder()
                .name("전강훈")
                .email("dreamingjas@gmail.com")
                .provider("kakao").build();

        userRepository.save(user);
    }

    @Test
    @DisplayName("이메일 전송 테스트")
    void emailSend() throws Exception {
        UserValidateEmailDTO userValidateEmailDTO = new UserValidateEmailDTO("corgiwalke@gmail.com");

        String json = objectMapper.writeValueAsString(userValidateEmailDTO);
        MvcResult result = mockmvc.perform(post("/api/user/validation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                        .andExpect(status().is(200))
                        .andReturn();
    }

    @Test
    @DisplayName("사용자 업데이트 테스트")
    void updateUser() throws Exception{

        UserLoginServerRequestDTO userLoginServerRequestDTO = new UserLoginServerRequestDTO("corgiwalke@gmail.com", "1q2w3e4r");

        String json = objectMapper.writeValueAsString(userLoginServerRequestDTO);
        MvcResult result = mockmvc.perform(post("/api/user/login-by-email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().is(200))
                .andReturn();

        UserUpdateAccountRequestDTO userUpdateAccountRequestDTO = new UserUpdateAccountRequestDTO("TestPassword", "TestDeparment","Ganghoon");
        json = objectMapper.writeValueAsString(userUpdateAccountRequestDTO);
        String responseContent = result.getResponse().getContentAsString();
        result = mockmvc.perform(put("/api/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .header("Authorization", "Bearer "+responseContent))
                .andExpect(status().is(204))
                .andReturn();

    }

    @Test
    @DisplayName("부서 입력 테스트")
    void initDepartment() throws Exception {

        UserLoginServerRequestDTO userLoginServerRequestDTO = new UserLoginServerRequestDTO("corgiwalke@gmail.com", "1q2w3e4r");

        String json = objectMapper.writeValueAsString(userLoginServerRequestDTO);
        MvcResult result = mockmvc.perform(post("/api/user/login-by-email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().is(200))
                .andReturn();

        UserDepartmentRequestDTO userDepartmentRequestDto = new UserDepartmentRequestDTO("Test");

        json = objectMapper.writeValueAsString(userDepartmentRequestDto);
        String responseContent = result.getResponse().getContentAsString();
        result = mockmvc.perform(patch("/api/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .header("Authorization", "Bearer "+responseContent))
                .andExpect(status().is(200))
                .andReturn();
    }

    @Test
    @DisplayName("로그인 테스트")
    void getTokenByLogin() throws Exception {


        UserLoginServerRequestDTO userLoginServerRequestDTO = new UserLoginServerRequestDTO("corgiwalke@gmail.com", "1q2w3e4r");

        String json = objectMapper.writeValueAsString(userLoginServerRequestDTO);
        MvcResult result = mockmvc.perform(post("/api/user/login-by-email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().is(200))
                .andReturn();
    }

    @Test
    @DisplayName("삭제 테스트")
    void deleteUser() throws  Exception {

        UserLoginServerRequestDTO userLoginServerRequestDTO = new UserLoginServerRequestDTO("corgiwalke@gmail.com", "1q2w3e4r");

        String json = objectMapper.writeValueAsString(userLoginServerRequestDTO);
        MvcResult result = mockmvc.perform(post("/api/user/login-by-email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().is(200))
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        result = mockmvc.perform(delete("/api/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .header("Authorization", "Bearer "+responseContent))
                .andExpect(status().is(200))
                .andReturn();
    }

    @Test
    void getTokenByOauth() {
    }

    @Test
    void getUserInformationList() throws Exception {
        UserLoginServerRequestDTO userLoginServerRequestDTO = new UserLoginServerRequestDTO("corgiwalke@gmail.com", "1q2w3e4r");

        String json = objectMapper.writeValueAsString(userLoginServerRequestDTO);
        MvcResult result = mockmvc.perform(post("/api/user/login-by-email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().is(200))
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        result = mockmvc.perform(get("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .header("Authorization", "Bearer "+responseContent))
                .andExpect(status().is(200))
                .andReturn();
    }

    @Test
    void getCurrentUserInformation() throws Exception {


        UserLoginServerRequestDTO userLoginServerRequestDTO = new UserLoginServerRequestDTO("corgiwalke@gmail.com", "1q2w3e4r");


        String json = objectMapper.writeValueAsString(userLoginServerRequestDTO);
        MvcResult result = mockmvc.perform(post("/api/user/login-by-email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().is(200))
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        result = mockmvc.perform(get("/api/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .header("Authorization", "Bearer "+responseContent))
                .andExpect(status().is(200))
                .andReturn();
    }
}