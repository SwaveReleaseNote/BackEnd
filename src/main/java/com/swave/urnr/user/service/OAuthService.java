package com.swave.urnr.user.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.swave.urnr.util.oauth.OauthToken;
import com.swave.urnr.util.oauth.kakao.KakaoProfile;
import com.swave.urnr.user.domain.User;
import com.swave.urnr.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

public interface OAuthService {


    OauthToken getOauthAccessToken(String code, String provider) throws RuntimeException;

    KakaoProfile findKakaoProfile(String token);

    String getTokenByOauth(String token, String provider);
}
