package com.swave.urnr.user.socketsystem;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.swave.urnr.user.domain.User;
import com.swave.urnr.user.exception.UserNotFoundException;
import com.swave.urnr.user.repository.UserRepository;
import com.swave.urnr.util.oauth.JwtProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptorAdapter;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
public class StompHandler extends ChannelInterceptorAdapter {
    private final UserRepository userRepository;

    public StompHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    private Map<String, Long> onlineUser = new HashMap<>();
    @Override
    public void postSend(Message message, MessageChannel channel, boolean sent) {

        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        StompHeaderAccessor headerAccessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        String token;
        List<String> authorizationHeaders;

        String sessionId = accessor.getSessionId();
        log.info(accessor.toString());
        log.info(message.toString());


        Optional<User> optionalUser;

        switch (accessor.getCommand()) {
            case CONNECT:
                // 유저가 Websocket으로 connect()를 한 뒤 호출됨
                authorizationHeaders = headerAccessor.getNativeHeader("Authorization");
                Long id = null;
                if (authorizationHeaders != null && !authorizationHeaders.isEmpty()) {
                    token = authorizationHeaders.get(0);
                    token = token.replace(JwtProperties.TOKEN_PREFIX, "");
                    log.info(token);
                    id = JWT.require(Algorithm.HMAC512(JwtProperties.SECRET)).build().verify(token).getClaim("id").asLong();
                    log.info(id.toString());
                    onlineUser.put(sessionId, id);
                } else {
                    log.warn("Authorization header is missing or empty.");
                }
                optionalUser = userRepository.findById(id);

                if (optionalUser.isPresent()) {
                    log.info("Sucussed for Filt but fail here. ");
                    User user = optionalUser.get();
                    user.setOnline(true);
                    log.info("Final : " + user);
                    userRepository.save(user);
                    userRepository.flush();
                }
                log.info(onlineUser.toString());
                log.info("Welcome connect"+sessionId);
                break;
            case DISCONNECT:
                // 유저가 Websocket으로 disconnect() 를 한 뒤 호출됨 or 세션이 끊어졌을 때 발생함(페이지 이동~ 브라우저 닫기 등)
                log.info(onlineUser.toString());
                log.info("Bye disconnect"+sessionId);
                Long deleteid=onlineUser.get(sessionId);
                log.info("Bye disconnect"+deleteid);
                optionalUser = userRepository.findById(deleteid);

                if (optionalUser.isPresent()) {
                    log.info("Sucussed for Filt but fail here. ");
                    User user = optionalUser.get();
                    user.setOnline(false);
                    log.info("Final : " + user);
                    userRepository.save(user);
                    userRepository.flush();
                }
                onlineUser.remove(sessionId);

                break;
            default:
                break;
        }

    }
}