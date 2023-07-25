package com.swave.urnr.user.service;

import com.swave.urnr.user.responsedto.UserListResponseDTO;
import com.swave.urnr.user.responsedto.UserResponseDTO;
import com.swave.urnr.util.common.ResponseDTO;
import com.swave.urnr.user.exception.UserNotFoundException;
import com.swave.urnr.user.requestdto.*;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


public interface UserService {

    ResponseEntity<ResponseDTO> createAccountByEmail(UserRegisterRequestDTO request);

    ResponseEntity<String> getValidationCode(UserValidateEmailDTO request) ;

    ResponseEntity<String> updateUser(HttpServletRequest request, UserUpdateAccountRequestDTO requestDto);

    ResponseEntity<ResponseDTO> initDepartment(HttpServletRequest request, UserDepartmentRequestDTO requestDto) ;
 
    ResponseEntity<String> getTokenByLogin(UserLoginServerRequestDTO requestDto) ;

    ResponseEntity<String> setTemporaryPassword(UserValidateEmailDTO request) ;

    ResponseEntity<String> deleteUser(HttpServletRequest request);

    UserResponseDTO getUser(HttpServletRequest request) throws UserNotFoundException;

    ResponseEntity<UserResponseDTO> getCurrentUserInformation(HttpServletRequest request) throws RuntimeException;

    ResponseEntity getTokenByOauth(String code, String provider) ;

    void checkInvalidToken(HttpServletRequest request) ;

    List<UserListResponseDTO> getUserInformationList();

    boolean updateLoginState(HttpServletRequest request, boolean requestDto) throws UserNotFoundException;
}
