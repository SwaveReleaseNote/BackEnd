package com.swave.urnr.user.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.swave.urnr.user.domain.User;
import com.swave.urnr.user.exception.UserNotFoundException;
import com.swave.urnr.user.repository.UserRepository;
import com.swave.urnr.user.requestdto.*;
import com.swave.urnr.user.responsedto.ManagerResponseDTO;
import com.swave.urnr.user.responsedto.UserResponseDTO;
import com.swave.urnr.user.responsedto.UserEntityResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Marker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
@RequiredArgsConstructor
class UserServiceImplTest {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MockMvc mockmvc;

    @Autowired
    private ObjectMapper objectMapper;

    private PasswordEncoder encoder = new BCryptPasswordEncoder();


    @BeforeEach
    @DisplayName("계정 생성 테스트")
    void createAccountByEmail() {

        userRepository.deleteAll();

        UserRegisterRequestDTO userRegisterRequestDTO = new UserRegisterRequestDTO("corgiwalke@gmail.com","1q2w3e4r","전강훈");

        ResponseEntity<UserEntityResponseDTO> result = userService.createAccountByEmail(userRegisterRequestDTO);

        User resultUser = userRepository.findByEmail("corgiwalke@gmail.com").orElseThrow(NoSuchElementException::new);

        assertEquals(encoder.matches(userRegisterRequestDTO.getPassword(),resultUser.getPassword()),true);
        assertEquals(userRegisterRequestDTO.getEmail(), resultUser.getEmail());
        assertEquals(userRegisterRequestDTO.getName(), resultUser.getUsername());
        assertEquals( result.getStatusCode().value() , 201);
        assertEquals( result.getBody().getData(),"User created");

        log.info("User create completed");
    }

    @Test
    @DisplayName("소속 수정 테스트")
    @Transactional
    void initDepartment() {

        MockHttpServletRequest request = new MockHttpServletRequest();
        User user = userRepository.findByEmail("corgiwalke@gmail.com").get();
        request.setAttribute("id", user.getId());

        UserDepartmentRequestDTO userDepartmentRequestDTO = new UserDepartmentRequestDTO("test");

        ResponseEntity<UserEntityResponseDTO> result = userService.initDepartment(request,userDepartmentRequestDTO);

        UserEntityResponseDTO userEntityResponseDTO = new UserEntityResponseDTO(200,"test");

        assertEquals(user.getDepartment() , userDepartmentRequestDTO.getDepartment());
        assertEquals( result.getStatusCode().value() , 200);
        assertEquals( result.getBody().getData(), userEntityResponseDTO.getData());
    }

    @Test
    @DisplayName("인증코드 반환 테스트")
    void getValidationCode() {

        UserValidateEmailDTO request = new UserValidateEmailDTO("artisheep@naver.com");
        /*
        테스트의 경우 , 클라우드 import 이전까지 비밀번호 대책강구 필요.
         */

//        ResponseEntity<String> result = userService.getValidationCode(request);

//        assertEquals( result.getStatusCode().value() , 200);

    }

    @Test
    @DisplayName("사용자 정보 반환 테스트")
    void getUser() throws UserNotFoundException {

        MockHttpServletRequest request = new MockHttpServletRequest();
        User user = userRepository.findByEmail("corgiwalke@gmail.com").get();
        request.setAttribute("id", user.getId());
        UserResponseDTO resultExcepted = new UserResponseDTO(
                user.getId(),
                user.getEmail(),
                user.getDepartment(),
                user.getUsername() );


        UserResponseDTO result = userService.getUser(request);

        assertEquals( resultExcepted.getId() , result.getId());
        assertEquals( resultExcepted.getDepartment(), result.getDepartment() );
        assertEquals(resultExcepted.getEmail(), result.getEmail());
        assertEquals(resultExcepted.getUsername() , result.getUsername());

    }

    @Test
    @DisplayName("현재 유저 정보 반환 테스트")
    void getCurrentUserInformation() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        User user = userRepository.findByEmail("corgiwalke@gmail.com").get();
        request.setAttribute("id", user.getId());
        request.addHeader("Authorization","test");
        ResponseEntity<UserResponseDTO> returnedUser = userService.getCurrentUserInformation(request);

        assertEquals( returnedUser.getStatusCode().value(), 200);
        UserResponseDTO userInfo = returnedUser.getBody();
        assertEquals(userInfo.getUsername(), user.getUsername());
        assertEquals(userInfo.getId(), user.getId());
        assertEquals(userInfo.getDepartment(), user.getDepartment());
        assertEquals(userInfo.getEmail(), user.getEmail());

    }

    @Test
    @DisplayName("토큰 확인 테스트")
    void checkInvalidToken() {


        UserLoginServerRequestDTO userLoginServerRequestDTO = new UserLoginServerRequestDTO("corgiwalke@gmail.com", "1q2w3e4r");


        ResponseEntity<String> result =  userService.getTokenByLogin(userLoginServerRequestDTO);

        MockHttpServletRequest request = new MockHttpServletRequest();
        User user = userRepository.findByEmail("corgiwalke@gmail.com").get();
        request.addHeader("Authorization",result.getBody().toString());
        userService.checkInvalidToken(request); // No throw = test complete

    }

    @Test
    @DisplayName("사용자 정보 리스트 반환 테스트")
    void getUserInformationList() throws JsonProcessingException {

        UserLoginServerRequestDTO userLoginServerRequestDTO = new UserLoginServerRequestDTO("corgiwalke@gmail.com", "1q2w3e4r");


        ResponseEntity<String> result =  userService.getTokenByLogin(userLoginServerRequestDTO);

        MockHttpServletRequest request = new MockHttpServletRequest();
        User user = userRepository.findByEmail("corgiwalke@gmail.com").get();
        request.setAttribute("id", user.getId());
        request.addHeader("Authorization", result.getBody());
        ManagerResponseDTO resultF = userService.getUserInformationList(request);

        log.info("RESULT HERE "+ objectMapper.writeValueAsString(resultF));

        assertEquals(user.getId(), resultF.getManagerId());
        assertEquals(user.getUsername(), resultF.getManagerName());
        assertEquals(user.getDepartment(), resultF.getManagerDepartment());
        assertEquals(user.getId(), resultF.getUsers().get(0).getUserId());
        assertEquals(user.getUsername(), resultF.getUsers().get(0).getUsername());
        assertEquals(user.getDepartment(), resultF.getUsers().get(0).getUserDepartment());

    }

    @Test
    @DisplayName("로그인 테스트")
    void getTokenByLogin() {

        UserLoginServerRequestDTO userLoginServerRequestDTO = new UserLoginServerRequestDTO("corgiwalke@gmail.com", "1q2w3e4r");
        ResponseEntity<String> result =  userService.getTokenByLogin(userLoginServerRequestDTO);
        assertEquals( result.getStatusCode().value(), 200);
    }

    @Test
    void getTokenByOauth() {

    }

    @Test
    @DisplayName("사용자 정보 업데이트 테스트")
    @Transactional
    void updateUser() {

        MockHttpServletRequest request = new MockHttpServletRequest();
        User user = userRepository.findByEmail("corgiwalke@gmail.com").get();
        request.setAttribute("id", user.getId());

        UserUpdateAccountRequestDTO userUpdateAccountRequestDTO = new UserUpdateAccountRequestDTO("TestPassword", "TestDeparment","Ganghoon");


        ResponseEntity<String> updateUser = userService.updateUser(request, userUpdateAccountRequestDTO);


        assertEquals( updateUser.getStatusCode().value(), 204);

    }

    @Test
    @DisplayName("임시 비밀번호 발급 테스트")
    @Transactional
    void setTemporaryPassword() {

        /*
         마찬가지로 smtp 메일 보안 이슈 해결 후까지 테스트 미진행 예정
         */
        UserValidateEmailDTO userValidateEmailDTO = new UserValidateEmailDTO("corgiwalke@gmail.com");
//        ResponseEntity<String> result = userService.setTemporaryPassword(userValidateEmailDTO);

//        User user = userRepository.findByEmail("corgiwalke@gmail.com").get();


//        assertEquals( result.getStatusCode().value(), 200);
//
//        assertEquals(encoder.matches(user.getPassword(),result.getBody()),true);

    }

    @Test
    @DisplayName("사용자 삭제 테스트")
    @Transactional
    void deleteUser() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        User user = userRepository.findByEmail("corgiwalke@gmail.com").get();
        request.setAttribute("id", user.getId());

        ResponseEntity<String> result = userService.deleteUser(request);

        assertEquals( result.getStatusCode().value(), 200);

        boolean isPresent = userRepository.findByEmail(user.getEmail()).isPresent();
        assertEquals(isPresent, false );

    }
}