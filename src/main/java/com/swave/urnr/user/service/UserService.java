package com.swave.urnr.user.service;

import com.swave.urnr.user.responsedto.ManagerResponseDTO;
import com.swave.urnr.user.responsedto.UserResponseDTO;
import com.swave.urnr.user.responsedto.UserEntityResponseDTO;
import com.swave.urnr.user.requestdto.*;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.servlet.http.HttpServletRequest;


public interface UserService {

    ResponseEntity<UserEntityResponseDTO> createAccountByEmail(UserRegisterRequestDTO request);

    ResponseEntity<String> getValidationCode(UserValidateEmailDTO request) ;

    ResponseEntity<String> updateUser(HttpServletRequest request, UserUpdateAccountRequestDTO requestDto);

    ResponseEntity<UserEntityResponseDTO> patchUserInformation(HttpServletRequest request, UserDepartmentRequestDTO requestDto) ;


    ResponseEntity<String> getTokenByLogin(UserLoginServerRequestDTO requestDto) ;

    ResponseEntity<String> setTemporaryPassword(UserValidateEmailDTO request) ;

    ResponseEntity<String> deleteUser(HttpServletRequest request);
    UserResponseDTO getUser(HttpServletRequest request) ;

    ResponseEntity<UserResponseDTO> getCurrentUserInformation(HttpServletRequest request) ;

    ResponseEntity<String> getTokenByOauth(String code, String provider) ;

    void checkInvalidToken(HttpServletRequest request) ;

    ManagerResponseDTO getUserInformationList(HttpServletRequest request);

    boolean updateLoginState(HttpServletRequest request, boolean requestDto) ;

    void createSampleAccount();

    @Transactional
    void deleteSampleAccount();

    ResponseEntity<String> updatePassword(HttpServletRequest request, UserUpdateAccountRequestDTO requestDto) ;

}
