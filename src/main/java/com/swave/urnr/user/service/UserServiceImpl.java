package com.swave.urnr.user.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.swave.urnr.util.common.response.ResponseDto;
import com.swave.urnr.util.exception.InvalidTokenException;
import com.swave.urnr.util.oauth.JwtProperties;
import com.swave.urnr.util.oauth.OauthToken;
import com.swave.urnr.user.domain.User;
import com.swave.urnr.user.exception.UserNotFoundException;
import com.swave.urnr.user.mailsystem.MailSendImp;
import com.swave.urnr.user.repository.UserRepository;
import com.swave.urnr.user.requestdto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final OAuthService oAuthService;
    private final MailSendImp mailSendImp;
    private final TokenService tokenService;
    @Override
    public ResponseEntity<ResponseDto> createAccountByServer(UserRegisterRequestDto request) {

        ResponseDto testDto;
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            testDto= ResponseDto.builder()
                    .status(409)
                    .data("The mail already exists")
                    .build();
            return ResponseEntity.status(409).body(testDto);
        }

        User user = User.builder()
                .email(request.getEmail())
                .password(request.getPassword())
                .name(request.getName())
                .provider("server")
                .build();

        userRepository.save(user);
        userRepository.flush();
        testDto= ResponseDto.builder()
                .status(201)
                .data(user)
                .build();
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().build().toUri();
        return ResponseEntity.created(location).body(testDto);

    }

    @Override
    public String initDepartment(HttpServletRequest request, String department) throws UserNotFoundException {

        Long id = (Long) request.getAttribute("id");
        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isPresent()) {
            log.info("Sucussed for Filt but fail here. " + department);
            User user = optionalUser.get();
            user.setDepartment(department);
            log.info("Final : " + user);
            userRepository.save(user);
            userRepository.flush();
        } else {
            throw new UserNotFoundException();

        }
        return department;
    }


    @Override
    public ResponseEntity<String> getValidationCode(UserValidateEmailDTO request) throws Exception {

        String email = request.getEmail();
        Boolean result =userRepository.findByEmail(email).isPresent();
        String code = "The Email Already exist";
        if (result) {
            return ResponseEntity.ok().body(code);
        }
        code = mailSendImp.sendSimpleMessage(email);
        return ResponseEntity.ok().body(code);
    }


    @Override
    public User getUser(HttpServletRequest request) throws UserNotFoundException {
        Long userCode = (Long) request.getAttribute("id");
        User user = userRepository.findById(userCode).orElseThrow(UserNotFoundException::new);
        return user;
    }


    @Override
    public ResponseEntity<Object> getCurrentUserInformation(HttpServletRequest request) throws InvalidTokenException, UserNotFoundException {
        checkInvalidToken(request);
        User user = getUser(request);
        System.out.println(user.getEmail());
        return ResponseEntity.ok().body(user);
    }


    @Override
    public void checkInvalidToken(HttpServletRequest request) throws InvalidTokenException {
        if (request.getHeader("Authorization") == null) {
            throw new InvalidTokenException();
        }
    }

    @Override
    public List getUserInformationList(){
        return userRepository.findAllUser();
    }

    @Override
    public String getTokenByLogin(HttpServletRequest request, UserLoginServerRequestDTO requestDto) throws UserNotFoundException {

        String email = requestDto.getEmail();
        Optional<User> optionalUser = userRepository.findByEmail(email);
        String result = "Information Not valid";
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (user.getPassword().equals(requestDto.getPassword())) {
                result =  tokenService.createToken(user);
            }
        }

        return result;
    }

    @Override
    public ResponseEntity getTokenByOauth(String code, String provider) throws JsonProcessingException {
        OauthToken oauthToken = oAuthService.getOauthAccessToken(code, provider);
        String jwtToken = oAuthService.getTokenByOauth(oauthToken.getAccess_token(), provider);
        HttpHeaders headers = new HttpHeaders();
        headers.add(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + jwtToken);
        return ResponseEntity.ok().headers(headers).body("\"success\"");
    }



    @Override
    public ResponseEntity<ResponseDto> updateUser(HttpServletRequest request, UserUpdateAccountRequestDto requestDto) throws UserNotFoundException {


        ResponseDto testDto;
        Long id = (Long) request.getAttribute("id");
        if(!userRepository.findById(id).isPresent())
        {
            testDto= ResponseDto.builder()
                    .status(404)
                    .data("The mail not exists")
                    .build();
            return ResponseEntity.status(404).body(testDto);
        }
        User user =  userRepository.findById(id).get();

        user.setPassword(requestDto.getPassword());
        user.setDepartment(requestDto.getDepartment());
        user.setUsername(requestDto.getName());

        log.info("Final : " + user);
        userRepository.save(user);
        userRepository.flush();

        testDto= ResponseDto.builder()
                .status(201)
                .data(user)
                .build();
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().build().toUri();

        return ResponseEntity.created(location).body(testDto);
    }

    @Override
    public ResponseEntity<String> setTemporaryPassword(UserValidateEmailDTO request) throws Exception {

        String email = request.getEmail();
        Boolean result = userRepository.findByEmail(email).isPresent();
        String code = "The Email doesn't exist";
        if (!result) {
            return ResponseEntity.ok().body(code);
        }
        code = mailSendImp.sendSimpleMessage(email);
        User user =  userRepository.findByEmail(email).get();
        user.setPassword(code);

        log.info("Final : " + user);
        userRepository.save(user);
        userRepository.flush();
        return ResponseEntity.ok().body(code);
    }
    @Override
    public void deleteUser(HttpServletRequest request) throws UserNotFoundException {
        Long id = (Long) request.getAttribute("id");
        userRepository.deleteById(id);
    }




}
