package com.swave.urnr.user.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
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
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
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
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
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
    private final RedissonClient redissonClient;


    public PasswordEncoder encoder = new BCryptPasswordEncoder();

    @Override
    @Transactional
    public ResponseEntity<UserEntityResponseDTO> createAccountByEmail(UserRegisterRequestDTO request) {



        UserEntityResponseDTO userEntityResponseDTO;
        log.info("Email : ", request.getEmail());

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            userEntityResponseDTO = new UserEntityResponseDTO(409, "The mail already exists");
            log.info("Email already exists");
            return ResponseEntity.status(409).body(userEntityResponseDTO);
        }

        User user = User.builder()
                .email(request.getEmail())
                .password(encoder.encode(request.getPassword()))
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
    public ResponseEntity<UserEntityResponseDTO> patchUserInformation(HttpServletRequest request, UserDepartmentRequestDTO requestDto)  {


            Long id = (Long) request.getAttribute("id");
            log.info(id.toString());

            UserEntityResponseDTO userEntityResponseDto;
            if (!userRepository.findById(id).isPresent()) {
                userEntityResponseDto = new UserEntityResponseDTO(409, "The account does not exists.");
                return ResponseEntity.status(409).body(userEntityResponseDto);
            }
            User user = userRepository.findById(id).get();
            if(requestDto.getDepartment() !=null){
                user.setDepartment(requestDto.getDepartment());
            }
            if(requestDto.getName() !=null){
                user.setUsername(requestDto.getName());
            }
            if(requestDto.getPassword() !=null){
                user.setPassword(encoder.encode(requestDto.getPassword()));
            }
            log.info("Final : " + user);
            userRepository.save(user);
            userRepository.flush();

            userEntityResponseDto = new UserEntityResponseDTO(200,"User Information patched.");
            return ResponseEntity.status(200).body(userEntityResponseDto);


    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<String> getValidationCode(UserValidateEmailDTO request)  {

        String email = request.getEmail();
        Boolean result = userRepository.findByEmail(email).isPresent();
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
        } catch (Exception e) {
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
        return new ManagerResponseDTO(user.getId(), user.getUsername(), user.getDepartment(), result);

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

        return ResponseEntity.status(409).body(null);
    }

    @Override
    @Transactional
    public ResponseEntity<SseEmitter> getTokenByOauth(String code, String provider) {
        log.info("hi1 : "+new Date());//203446 191
        System.out.println("SWAVE1 :"+new Date());
        OauthToken oauthToken = oAuthService.getOauthAccessToken(code, provider);
        log.info("hi2 : "+new Date());//203447

        System.out.println("SWAVE2 :"+new Date());
        String jwtToken = oAuthService.getTokenByOauth(oauthToken.getAccess_token(), provider);
        log.info("hi3 : "+new Date());//203447

        System.out.println("SWAVE3 :"+new Date());
        HttpHeaders headers = new HttpHeaders();
        log.info("hi4 : "+new Date());//203447

        System.out.println("SWAVE4 :"+new Date());
        headers.add(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + jwtToken);
        log.info("hi5 : "+new Date());//203447

        System.out.println("SWAVE5 :"+new Date());

        Long id = JWT.require(Algorithm.HMAC512(JwtProperties.SECRET)).build().verify(jwtToken).getClaim("id").asLong();
        log.info("hi6 : "+new Date());//203447

        System.out.println("SWAVE6 :"+new Date());
        SseEmitter sseEmitter = sseEmitterService.subscribeEmitter(String.valueOf(id), jwtToken);
        log.info("hi7 : "+new Date());//203447

        System.out.println("SWAVE7 :"+new Date());
        kafkaService.createTopic(id.toString());
        log.info("hi8 : "+new Date());//203448 179

        System.out.println("SWAVE8 :"+new Date());

        return  ResponseEntity.ok().body(sseEmitter);
    }

    @Override
    @Transactional
    public ResponseEntity<String> updateUser(HttpServletRequest request, UserUpdateAccountRequestDTO requestDto) {

        RLock lock = redissonClient.getLock("updateUser");
        try {
            boolean available = lock.tryLock(100, 2, TimeUnit.SECONDS);
            if (!available) {
                throw new RuntimeException("Lock 획득 실패!");
            }
            Long id = (Long) request.getAttribute("id");
            if (!userRepository.findById(id).isPresent()) {
                return ResponseEntity.status(404).body("User doesn't exist");
            }
            User user = userRepository.findById(id).get();

//        user.setPassword(encoder.encode(requestDto.getPassword()));
            user.setDepartment(requestDto.getDepartment());
            user.setUsername(requestDto.getName());

            log.info("Final : " + user);
            userRepository.save(user);
            userRepository.flush();

            return ResponseEntity.status(204).body("user data updated.");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
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
        User user = userRepository.findByEmail(email).get();
        user.setPassword(encoder.encode(code));

        log.info("Final : " + user);
        userRepository.save(user);
        userRepository.flush();
        return ResponseEntity.ok().body(code);
    }
    @Override
    @Transactional
       public ResponseEntity<String> deleteUser(HttpServletRequest request) {
        RLock lock = redissonClient.getLock("deleteUser");
        try {
            boolean available = lock.tryLock(100, 2, TimeUnit.SECONDS);
            if (!available) {
                throw new RuntimeException("Lock 획득 실패!");
            }
            Long id = (Long) request.getAttribute("id");
            if (userRepository.findById(id).isPresent()) {
                userRepository.deleteById(id);
                return ResponseEntity.ok().body("account deleted");
            } else {
                return ResponseEntity.status(404).body("userid not found");

            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
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

        String email = "rkdwnsgml@xptmxm.com";
        if(userRepository.findByEmail(email).isPresent()){
            userRepository.hardDeleteById(userRepository.findByEmail(email).get().getId());
            userRepository.flush();
        }

        userRepository.save(user);

        userRepository.flush();

        Long userId = userRepository.findByEmail(user.getEmail()).get().getId();
        log.info("Excepted Topic : "+userId.toString());
        kafkaService.createTopic(userId.toString());
        user = User.builder()
                .email("rlarlgus@xptmxm.com")
                .password(encoder.encode("123456"))
                .name("김기현")
                .provider("email")
                .build();


        email = "rlarlgus@xptmxm.com";
        if(userRepository.findByEmail(email).isPresent()){
            userRepository.hardDeleteById(userRepository.findByEmail(email).get().getId());
            userRepository.flush();
        }

        userRepository.save(user);

        userRepository.flush();

        userId = userRepository.findByEmail(user.getEmail()).get().getId();
        log.info("Excepted Topic : "+userId.toString());
        kafkaService.createTopic(userId.toString());
        user = User.builder()
                .email("wjsrkdgns@xptmxm.com")
                .password(encoder.encode("admin"))
                .name("전강훈")
                .provider("email")
                .build();

        email = "wjsrkdgns@xptmxm.com";
        if(userRepository.findByEmail(email).isPresent()){
            userRepository.hardDeleteById(userRepository.findByEmail(email).get().getId());
            userRepository.flush();
        }

        userRepository.save(user);

        userRepository.flush();

        userId = userRepository.findByEmail(user.getEmail()).get().getId();
        log.info("Excepted Topic : "+userId.toString());
        kafkaService.createTopic(userId.toString());
        user = User.builder()
                .email("gkarjsdnr@xptmxm.com")
                .password(encoder.encode("admin"))
                .name("함건욱")
                .provider("email")
                .build();



        email = "gkarjsdnr@xptmxm.com";
        if(userRepository.findByEmail(email).isPresent()){
            userRepository.hardDeleteById(userRepository.findByEmail(email).get().getId());
            userRepository.flush();
        }

        userRepository.save(user);

        userRepository.flush();

         userId = userRepository.findByEmail(user.getEmail()).get().getId();
        log.info("Excepted Topic : "+userId.toString());
        kafkaService.createTopic(userId.toString());

        user = User.builder()
                .email("rlatjdrnr@xptmxm.com")
                .password(encoder.encode("admin"))
                .name("김성국")
                .provider("email")
                .build();


        email = "rlatjdrnr@xptmxm.com";
        if(userRepository.findByEmail(email).isPresent()){
            userRepository.hardDeleteById(userRepository.findByEmail(email).get().getId());
            userRepository.flush();
        }
        userRepository.save(user);

        userRepository.flush();

        userId = userRepository.findByEmail(user.getEmail()).get().getId();
        log.info("Excepted Topic : "+userId.toString());
        kafkaService.createTopic(userId.toString());
        user = User.builder()
                .email("dltmdtjq@xptmxm.com")
                .password(encoder.encode("admin"))
                .name("이승섭")
                .provider("email")
                .build();



        email = "dltmdtjq@xptmxm.com";
        if(userRepository.findByEmail(email).isPresent()){
            userRepository.hardDeleteById(userRepository.findByEmail(email).get().getId());
            userRepository.flush();
        }
        userRepository.save(user);
        userRepository.flush();

        userId = userRepository.findByEmail(user.getEmail()).get().getId();
        log.info("Excepted Topic : "+userId.toString());
        kafkaService.createTopic(userId.toString());

    }


    @Override
    @Transactional
    public void deleteSampleAccount() {


        Long userId = userRepository.findByEmail("rkdwnsgml@xptmxm.com").get().getId();
        userRepository.hardDeleteById(userId);

        userId = userRepository.findByEmail("rlarlgus@xptmxm.com").get().getId();
        userRepository.hardDeleteById(userId);

        userId = userRepository.findByEmail("rlatjdrnr@xptmxm.com").get().getId();
        userRepository.hardDeleteById(userId);

        userId = userRepository.findByEmail("wjsrkdgns@xptmxm.com").get().getId();
        userRepository.hardDeleteById(userId);

        userId = userRepository.findByEmail("dltmdtjq@xptmxm.com").get().getId();
        userRepository.hardDeleteById(userId);

        userId = userRepository.findByEmail("gkarjsdnr@xptmxm.com").get().getId();
        userRepository.hardDeleteById(userId);
    }


    @Override
    @Transactional
    public ResponseEntity<String> updatePassword(HttpServletRequest request, UserUpdateAccountRequestDTO requestDto) {


        RLock lock = redissonClient.getLock("updatePassword");
        try {
            boolean available = lock.tryLock(100, 2, TimeUnit.SECONDS);
            if (!available) {
                throw new RuntimeException("Lock 획득 실패!");
            }
            Long id = (Long) request.getAttribute("id");
            if (!userRepository.findById(id).isPresent()) {
                return ResponseEntity.status(404).body("User doesn't exist");
            }
            User user = userRepository.findById(id).get();

            user.setPassword(encoder.encode(requestDto.getPassword()));

            log.info("Final : " + user);
            userRepository.save(user);
            userRepository.flush();

            return ResponseEntity.status(204).body("Updated User data");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }
}
