package com.swave.urnr.user.service;

import com.swave.urnr.user.responsedto.ManagerResponseDTO;
import com.swave.urnr.user.responsedto.UserListResponseDTO;
import com.swave.urnr.user.responsedto.UserResponseDTO;
import com.swave.urnr.user.responsedto.UserEntityResponseDTO;
import com.swave.urnr.util.kafka.KafkaService;
import com.swave.urnr.util.oauth.JwtProperties;
import com.swave.urnr.util.oauth.OauthToken;
import com.swave.urnr.user.domain.User;
import com.swave.urnr.user.exception.UserNotFoundException;
import com.swave.urnr.user.mailsystem.MailSendImp;
import com.swave.urnr.user.repository.UserRepository;
import com.swave.urnr.user.requestdto.*;
import com.swave.urnr.util.sse.SSEEmitterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.List;
import java.util.NoSuchElementException;
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
    private final KafkaService kafkaService;

    private final SSEEmitterService sseEmitterService;

    public PasswordEncoder encoder = new BCryptPasswordEncoder();

    @Override
    @Transactional
    public ResponseEntity<UserEntityResponseDTO> createAccountByEmail(UserRegisterRequestDTO request) {



        UserEntityResponseDTO userEntityResponseDTO;
        log.info("Email : ", request.getEmail());

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            userEntityResponseDTO= new UserEntityResponseDTO(409,"The mail already exists");
            log.info("Email already exists");
            return ResponseEntity.status(409).body(userEntityResponseDTO);
        }

        User user = User.builder()
                .email(request.getEmail())
                .password(encoder.encode( request.getPassword()))
                .name(request.getName())
                .provider("email")
                .build();

        userRepository.save(user);
        userRepository.flush();

        Long userId = userRepository.findByEmail(request.getEmail()).get().getId();
        log.info("Excepted Topic : "+userId.toString());
        kafkaService.createTopic(userId.toString());

        log.info("made email and topic for that email.");

        userEntityResponseDTO= new UserEntityResponseDTO(201,"User created");
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().build().toUri();
        return ResponseEntity.created(location).body(userEntityResponseDTO);

    }

    @Override
    @Transactional
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
    @Transactional(readOnly = true)
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
    @Transactional(readOnly = true)
    public UserResponseDTO getUser(HttpServletRequest request)  {

        Long userCode = (Long) request.getAttribute("id");
        User user = userRepository.findById(userCode).orElseThrow(NoSuchElementException::new);
        UserResponseDTO result = new UserResponseDTO(
                user.getId(),
                user.getEmail(),
                user.getDepartment(),
                user.getUsername());

        return result;

    }



    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<UserResponseDTO> getCurrentUserInformation(HttpServletRequest request)  {
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
    public void checkInvalidToken(HttpServletRequest request) {
        if (request.getHeader("Authorization") == null) {
            throw new NoSuchElementException("Authorization 토큰이 없습니다. ");

        }
    }

    @Override
    @Transactional(readOnly = true)
    public ManagerResponseDTO getUserInformationList(HttpServletRequest request) {

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
    @Transactional(readOnly = true)
    public ResponseEntity<SseEmitter> getTokenByLogin(UserLoginServerRequestDTO requestDto)  {


        String email = requestDto.getEmail();
        Optional<User> optionalUser = userRepository.findByEmail(email);
        String result = "Information Not valid";
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (encoder.matches( requestDto.getPassword(),user.getPassword())){

                String token = tokenService.createToken(user);
                log.info("TOKEN AQUIRED : {}", token);
                log.info("UI AQUIRED : {}", user.getId().toString());
                SseEmitter sseEmitter = sseEmitterService.subscribeEmitter(String.valueOf(user.getId()), token);

                log.info(" SSE AQUIRED? ");

                return  ResponseEntity.ok().body(sseEmitter);
            }
        }

        return ResponseEntity.status(409).body(new SseEmitter() );
    }

    @Override
    @Transactional
    public ResponseEntity<SseEmitter> getTokenByOauth(String code, String provider) {
        OauthToken oauthToken = oAuthService.getOauthAccessToken(code, provider);
        String jwtToken = oAuthService.getTokenByOauth(oauthToken.getAccess_token(), provider);
        HttpHeaders headers = new HttpHeaders();
        headers.add(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + jwtToken);


        SseEmitter sseEmitter = sseEmitterService.subscribeEmitter(String.valueOf("UserID need to here"), jwtToken);

        return  ResponseEntity.ok().body(sseEmitter);
    }

    @Override
    @Transactional
    public ResponseEntity<String> updateUser(HttpServletRequest request, UserUpdateAccountRequestDTO requestDto) {

        Long id = (Long) request.getAttribute("id");
        if(!userRepository.findById(id).isPresent())
        {
            return ResponseEntity.status(404).body("User doesn't exist");
        }
        User user =  userRepository.findById(id).get();

//        user.setPassword(encoder.encode(requestDto.getPassword()));
        user.setDepartment(requestDto.getDepartment());
        user.setUsername(requestDto.getName());

        log.info("Final : " + user);
        userRepository.save(user);
        userRepository.flush();

        return ResponseEntity.status(204).body("Updated User data");

    }

    @Override
    @Transactional
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
    @Transactional
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
    @Transactional
    public boolean updateLoginState(HttpServletRequest request, boolean loginState)  {
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
            throw new NoSuchElementException();

        }
        return loginState;
    }


    @Override
    @Transactional
    public void createSampleAccount() {

        User user = User.builder()
                .email("rkdwnsgml@xptmxm.com")
                .password(encoder.encode("123456"))
                .name("강준희")
                .provider("email")
                .build();

        userRepository.save(user);
        user = User.builder()
                .email("rlarlgus@xptmxm.com")
                .password(encoder.encode("123456"))
                .name("김기현")
                .provider("email")
                .build();

        userRepository.save(user);

        user = User.builder()
                .email("wjsrkdgns@xptmxm.com")
                .password(encoder.encode("admin"))
                .name("전강훈")
                .provider("email")
                .build();

        userRepository.save(user);

        user = User.builder()
                .email("gkarjsdnr@xptmxm.com")
                .password(encoder.encode("admin"))
                .name("함건욱")
                .provider("email")
                .build();

        userRepository.save(user);


        user = User.builder()
                .email("rlatjdrnr@xptmxm.com")
                .password(encoder.encode("admin"))
                .name("김성국")
                .provider("email")
                .build();

        userRepository.save(user);

        user = User.builder()
                .email("dltmdtjq@xptmxm.com")
                .password(encoder.encode("admin"))
                .name("이승섭")
                .provider("email")
                .build();

        userRepository.save(user);
        userRepository.flush();

        Long userId = userRepository.findByEmail("rkdwnsgml@xptmxm.com").get().getId();
        kafkaService.createTopic(userId.toString());

        userId = userRepository.findByEmail("rlarlgus@xptmxm.com").get().getId();
        kafkaService.createTopic(userId.toString());

        userId = userRepository.findByEmail("rlatjdrnr@xptmxm.com").get().getId();
        kafkaService.createTopic(userId.toString());

        userId = userRepository.findByEmail("wjsrkdgns@xptmxm.com").get().getId();
        kafkaService.createTopic(userId.toString());

        userId = userRepository.findByEmail("dltmdtjq@xptmxm.com").get().getId();
        kafkaService.createTopic(userId.toString());

        userId = userRepository.findByEmail("gkarjsdnr@xptmxm.com").get().getId();
        kafkaService.createTopic(userId.toString());
    }


    @Override
    @Transactional
    public ResponseEntity<String> updatePassword(HttpServletRequest request, UserUpdateAccountRequestDTO requestDto) {

        Long id = (Long) request.getAttribute("id");
        if(!userRepository.findById(id).isPresent())
        {
            return ResponseEntity.status(404).body("User doesn't exist");
        }
        User user =  userRepository.findById(id).get();

        user.setPassword(encoder.encode(requestDto.getPassword()));

        log.info("Final : " + user);
        userRepository.save(user);
        userRepository.flush();

        return ResponseEntity.status(204).body("Updated User data");

    }
}
