package com.swave.urnr.user.service;

import com.swave.urnr.user.responsedto.ManagerResponseDTO;
import com.swave.urnr.user.responsedto.UserListResponseDTO;
import com.swave.urnr.user.responsedto.UserResponseDTO;
import com.swave.urnr.user.responsedto.UserEntityResponseDTO;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final OAuthService oAuthService;
    private final MailSendImp mailSendImp;
    private final TokenService tokenService;


    public PasswordEncoder encoder = new BCryptPasswordEncoder();


    @Override
    public ResponseEntity<UserEntityResponseDTO> createAccountByEmail(UserRegisterRequestDTO request) {

        UserEntityResponseDTO userEntityResponseDTO;
        log.info("Email : ", request.getEmail());

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            userEntityResponseDTO= new UserEntityResponseDTO(409,"The mail already exists");
            log.info("Email already exists");
            return ResponseEntity.status(409).body(userEntityResponseDTO);
        }
        log.info("Email not already exists, builded it. ");

        User user = User.builder()
                .email(request.getEmail())
                .password(encoder.encode( request.getPassword()))
                .name(request.getName())
                .provider("email")
                .build();

        userRepository.save(user);
        userRepository.flush();

        userEntityResponseDTO= new UserEntityResponseDTO(201,"User created");
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().build().toUri();
        return ResponseEntity.created(location).body(userEntityResponseDTO);

    }

    @Override
    public ResponseEntity<UserEntityResponseDTO> initDepartment(HttpServletRequest request, UserDepartmentRequestDTO requestDto)  {

        Long id = (Long) request.getAttribute("id");
        log.info(id.toString());

        UserEntityResponseDTO userEntityResponseDto;
        if (!userRepository.findById(id).isPresent()) {
            userEntityResponseDto = new UserEntityResponseDTO(409,"The account does not exists.");
            return ResponseEntity.status(409).body(userEntityResponseDto);
        }
        User user = userRepository.findById(id).get();
            user.setDepartment(requestDto.getDepartment());
            log.info("Final : " + user);
            userRepository.save(user);
            userRepository.flush();

            userEntityResponseDto = new UserEntityResponseDTO(200,user.getDepartment());
            return ResponseEntity.status(200).body(userEntityResponseDto);

    }

    @Override
    public ResponseEntity<String> getValidationCode(UserValidateEmailDTO request)  {

        String email = request.getEmail();
        Boolean result =userRepository.findByEmail(email).isPresent();
        String code = "The Email Already exist";
        if (result) {
            return ResponseEntity.ok().body(code);
        }
        code = mailSendImp.sendCodeMessage(email);
        return ResponseEntity.ok().body(code);
    }

    @Override
    public UserResponseDTO getUser(HttpServletRequest request) throws UserNotFoundException {
        Long userCode = (Long) request.getAttribute("id");
        User user = userRepository.findById(userCode).orElseThrow(UserNotFoundException::new);
        UserResponseDTO result = new UserResponseDTO(
                user.getId(),
                user.getEmail(),
                user.getDepartment(),
                user.getUsername());
        return result;
    }



    @Override
    public ResponseEntity<UserResponseDTO> getCurrentUserInformation(HttpServletRequest request) throws  RuntimeException {
        UserResponseDTO user =null;
        try {
            checkInvalidToken(request);
             user = getUser(request);
        }catch(Exception e)
        {
            e.printStackTrace();
            log.info(e.toString());
        }
        return ResponseEntity.ok().body(user);
    }


    @Override
    public void checkInvalidToken(HttpServletRequest request) throws RuntimeException {
        if (request.getHeader("Authorization") == null) {
            throw new RuntimeException();
        }
    }

    @Override
    public ManagerResponseDTO getUserInformationList(HttpServletRequest request) throws UserNotFoundException{

        Long userCode = (Long) request.getAttribute("id");
        User user = userRepository.findById(userCode).orElseThrow(UserNotFoundException::new);



        List<UserListResponseDTO> result =  userRepository.findAll().stream().map(
            User -> {
                UserListResponseDTO userListResponseDTO = new UserListResponseDTO(User.getId(), User.getUsername(), User.getDepartment());
                return userListResponseDTO;
            }
        ).collect(Collectors.toList());
        return new ManagerResponseDTO(user.getId(),user.getUsername(),user.getDepartment(),result);

    }


    @Override
    public ResponseEntity<String> getTokenByLogin(UserLoginServerRequestDTO requestDto)  {

        String email = requestDto.getEmail();
        Optional<User> optionalUser = userRepository.findByEmail(email);
        String result = "Information Not valid";
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (encoder.matches( requestDto.getPassword(),user.getPassword())){
                return  ResponseEntity.ok().body(tokenService.createToken(user));
            }
        }

        return ResponseEntity.status(409).body("Invalid Information");
    }

    @Override
    public ResponseEntity getTokenByOauth(String code, String provider) {
        OauthToken oauthToken = oAuthService.getOauthAccessToken(code, provider);
        String jwtToken = oAuthService.getTokenByOauth(oauthToken.getAccess_token(), provider);
        HttpHeaders headers = new HttpHeaders();
        headers.add(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + jwtToken);
        return ResponseEntity.ok().headers(headers).body("\"success\"");
    }



    @Override
    public ResponseEntity<String> updateUser(HttpServletRequest request, UserUpdateAccountRequestDTO requestDto) {

        Long id = (Long) request.getAttribute("id");
        if(!userRepository.findById(id).isPresent())
        {
            return ResponseEntity.status(404).body("User doesn't exist");
        }
        User user =  userRepository.findById(id).get();

        user.setPassword(encoder.encode(requestDto.getPassword()));
        user.setDepartment(requestDto.getDepartment());
        user.setUsername(requestDto.getName());

        log.info("Final : " + user);
        userRepository.save(user);
        userRepository.flush();

        return ResponseEntity.status(204).body("Updated User data");
    }

    @Override
    public ResponseEntity<String> setTemporaryPassword(UserValidateEmailDTO request)  {

        String email = request.getEmail();
        Boolean result = userRepository.findByEmail(email).isPresent();
        String code = "The Email doesn't exist";
        if (!result) {
            return ResponseEntity.ok().body(code);
        }
        code = mailSendImp.sendCodeMessage(email);
        User user =  userRepository.findByEmail(email).get();
        user.setPassword(encoder.encode(code));

        log.info("Final : " + user);
        userRepository.save(user);
        userRepository.flush();
        return ResponseEntity.ok().body(code);
    }
    @Override
    public ResponseEntity<String> deleteUser(HttpServletRequest request)  {
        Long id = (Long) request.getAttribute("id");
        if(userRepository.findById(id).isPresent())
        {userRepository.deleteById(id);
        return ResponseEntity.ok().body("deleted account");
        }
        else{
            return ResponseEntity.status(404).body("userid not found");

        }
    }

    @Override
    public boolean updateLoginState(HttpServletRequest request, boolean loginState) throws UserNotFoundException {
        Long id = (Long) request.getAttribute("id");
        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isPresent()) {
            log.info("Sucussed for Filt but fail here. " + loginState);
            User user = optionalUser.get();
            user.setOnline(loginState);
            log.info("Final : " + user);
            userRepository.save(user);
            userRepository.flush();
        } else {
            throw new UserNotFoundException();

        }
        return loginState;
    }


}
