package com.swave.urnr.user.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.swave.urnr.util.common.response.ResponseDto;
import com.swave.urnr.util.exception.InvalidTokenException;
import com.swave.urnr.user.domain.User;
import com.swave.urnr.user.exception.UserNotFoundException;
import com.swave.urnr.user.requestdto.*;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


public interface UserService {

    ResponseEntity<ResponseDto> createAccountByServer(UserRegisterRequestDto request);

    ResponseEntity<String> getValidationCode(UserValidateEmailDTO request) throws Exception;

    ResponseEntity<ResponseDto> updateUser(HttpServletRequest request, UserUpdateAccountRequestDto requestDto) throws UserNotFoundException;

    String initDepartment(HttpServletRequest request, String requestDto) throws UserNotFoundException;

    String getTokenByLogin(HttpServletRequest request, UserLoginServerRequestDTO requestDto) throws UserNotFoundException;

    ResponseEntity<String> setTemporaryPassword(UserValidateEmailDTO request) throws Exception;

    void deleteUser(HttpServletRequest request) throws UserNotFoundException;

    User getUser(HttpServletRequest request) throws UserNotFoundException;

    ResponseEntity<Object> getCurrentUserInformation(HttpServletRequest request) throws InvalidTokenException, UserNotFoundException;

    ResponseEntity getTokenByOauth(String code, String provider) throws JsonProcessingException;

    void checkInvalidToken(HttpServletRequest request) throws InvalidTokenException;

    List getUserInformationList();
}
