package com.swave.urnr.user.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.swave.urnr.user.domain.UserUpdateRequest;
import com.swave.urnr.user.exception.InvalidIdException;
import com.swave.urnr.user.exception.UserNotFoundException;
import com.swave.urnr.user.repository.UserRepository;
import com.swave.urnr.user.request.*;
import com.swave.urnr.user.response.EmailCheckResponseDto;
import com.swave.urnr.user.response.LoginResponseDTO;
import com.swave.urnr.Util.OAuth.JwtProperties;
import com.swave.urnr.Util.OAuth.OauthToken;
import com.swave.urnr.Util.exception.InvalidTokenException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;



import com.swave.urnr.user.domain.User;
import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final OAuthService oAuthService;


    @Override
    public void test(HttpServletRequest request, RegisterRequestDto userRequest) throws InvalidIdException {

    }

    @Override
    public void testGET(HttpServletRequest request, RegisterRequestDto userRequest) throws InvalidIdException {

    }


    /*
    TODO: Make a update-holder
     */
    @Override
    public void createUser(RegisterRequestDto request) {
        User user = User.builder()
                .email(request.getEmail())
                .password(request.getPassword())
                .name(request.getName())
                .provider("local")
                .build();

        userRepository.save(user);

    }
@Override
    public EmailCheckResponseDto checkEmailBody(EmailCheckRequestDto request) {

        log.info(request.toString());
        String email = (String) request.getEmail();
        Boolean  result = userRepository.findByEmail(email).isPresent();

        EmailCheckResponseDto emailCheckResponseDto = EmailCheckResponseDto.builder()
                .isValid(result)
                .build();

        return emailCheckResponseDto;
    }
@Override
public String checkEmailString(String request) throws UserNotFoundException{
        String email = request;
        //log.info("User Email : "+email);
        Boolean value =userRepository.findByEmail(email).isEmpty() ;
    //log.info(value.toString() );
        if( request == null || userRepository.findByEmail(email).isEmpty()){
            //log.info("User Doesn't Have any Header or not valid account");
            throw new UserNotFoundException();
        }

    log.info("User Email is Valid : "+email);
        return email;
}
@Override
    public void updateUser(HttpServletRequest request, UpdateUserDto requestDto) throws UserNotFoundException{
        /*
        TODO: possible : Return Updated Token so user can regain his ID.
         */
    log.info("Sucussed for Filt but fail here. ");
        Long id  = (Long) request.getAttribute("id");
        Optional<User> optionalUser  = userRepository.findById(id);
        String newEmail = requestDto.getEmail();
        String newPassword = requestDto.getPassword();
        String newDepartment = requestDto.getDepartment();
        String newName = requestDto.getName();

        if(optionalUser.isPresent()){
            User user = optionalUser.get();
            if(newEmail != null && !newEmail.isEmpty()){
                user.setEmail(newEmail);
            }
            if(newPassword != null && !newPassword.isEmpty()){
                user.setPassword(newPassword);
            }
            if(newDepartment !=null && !newDepartment.isEmpty()){
                user.setDepartment(newDepartment);
            }
            if(newName !=null && !newName.isEmpty()){
                user.setName(newName);
            }
            log.info("Final : " + user.toString());
            userRepository.save(user);
        }
        else{
            throw new UserNotFoundException();

        }

}

    @Override
    public LoginResponseDTO userLogin(HttpServletRequest request, LoginRequestDTO requestDto) throws UserNotFoundException {
        String email = checkEmailString(requestDto.getEmail());
        Optional<User> optionalUser  = userRepository.findByEmail(email);
        String result = "Not matching ID";
        if(optionalUser.isPresent())
        {
            User user = optionalUser.get();
            log.info("User : "+ user.getPassword() + " request : " + requestDto.getPassword());
            if(user.getPassword().equals(requestDto.getPassword())){
                result = oAuthService.createToken(user);
            }
        }

LoginResponseDTO loginResponseDTO = LoginResponseDTO.builder()
        .result(result)
        .build();
        return loginResponseDTO;
    }

    @Override
    public void deleteUser(HttpServletRequest request, DeleteUserDto requestDto) throws UserNotFoundException{
/*
TODO: Make a validation for ID delete phase
 */
        Long id = (Long) request.getAttribute("id");
        userRepository.deleteById(id);

    }


// 승섭님 작업


    @Override
    public User getUser(HttpServletRequest request) throws UserNotFoundException {
        Long userCode = (Long) request.getAttribute("id");
        User user = userRepository.findById(userCode).orElseThrow(UserNotFoundException::new);
        return user;
    }

    @Override
    public ResponseEntity<Object> getCurrentUser(HttpServletRequest request) throws InvalidTokenException, UserNotFoundException {
        checkInvalidToken(request);
        User user = getUser(request);
        System.out.println(user.getEmail());
        return ResponseEntity.ok().body(user);
    }

    @Override
    public ResponseEntity getLogin(String code,String provider) throws JsonProcessingException {
        OauthToken oauthToken = oAuthService.getAccessToken(code, provider);
        String jwtToken = oAuthService.SaveUserAndGetToken(oauthToken.getAccess_token(), provider);
        HttpHeaders headers = new HttpHeaders();
        headers.add(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + jwtToken);
        return ResponseEntity.ok().headers(headers).body("\"success\"");
    }
    @Override
    public String updateMyPage(HttpServletRequest request, UserUpdateRequest userUpdateRequest) throws Exception {
        checkInvalidToken(request);
        Long userId =getUser(request).getId();
        Optional<User> optionalUser = userRepository.findById(userId);
        if(optionalUser.isPresent()){
            User user = optionalUser.get();
            user.setName(userUpdateRequest.getNickname());
            userRepository.save(user);
        }else {
            throw new Exception();
        }
        return "success";
    }

    @Override
    public void checkInvalidToken(HttpServletRequest request) throws InvalidTokenException {
        if(request.getHeader("Authorization") == null) {
//            log.info("error");
            throw new InvalidTokenException();
        }
//        log.info("토큰 체크 완료");
    }
}
