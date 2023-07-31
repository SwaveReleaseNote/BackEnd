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

@Slf4j
@Service
@RequiredArgsConstructor
public class OAuthService {

    private final UserRepository userRepository;
    private final TokenService tokenService;
    //TODO: Need to Custom Excepition
    public OauthToken getOauthAccessToken(String code, String provider) throws RuntimeException {
        RestTemplate rt = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        String grantType;
        String clientId;
        String clientSecret;
        String redirectUri;
        if (provider.equals("kakao")) {
            grantType = "authorization_code";
            clientId = "4646a32b25c060e42407ceb8c13ef14a";
            clientSecret = "AWyAH1M24R9EYfUjJ1KCxcsh3DwvK8F7";
            redirectUri = "http://localhost:3000/oauth/callback/kakao";
        } else {
            throw new RuntimeException("Invalid Provider: " + provider);
        }

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", grantType);
        params.add("client_id", clientId);
        params.add("redirect_uri", redirectUri);
        params.add("code", code);
        params.add("client_secret", clientSecret);

        HttpEntity<MultiValueMap<String, String>> tokenRequest = new HttpEntity<>(params, headers);

        ResponseEntity<String> tokenResponse = rt.exchange(
                getProviderTokenUrl(provider),
                HttpMethod.POST,
                tokenRequest,
                String.class
        );

        ObjectMapper objectMapper = new ObjectMapper();
        OauthToken oauthToken = null;
        try{
            oauthToken = objectMapper.readValue(tokenResponse.getBody(), OauthToken.class);
        }catch(Exception e){
            e.printStackTrace();

        }

        return oauthToken;
    }

    //provider에 따라 URL 제공 구분
    private String getProviderTokenUrl(String provider) {
        if (provider.equals("kakao")) {
            return "https://kauth.kakao.com/oauth/token";
        } else {
            throw new IllegalArgumentException("Invalid Provider: " + provider);
        }
    }

    public KakaoProfile findKakaoProfile(String token) {

        RestTemplate rt = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token); //(1-4)
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest =
                new HttpEntity<>(headers);

        // Http 요청 (POST 방식) 후, response 변수에 응답을 받음
        ResponseEntity<String> kakaoProfileResponse = rt.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoProfileRequest,
                String.class
        );

        ObjectMapper objectMapper = new ObjectMapper();
        KakaoProfile kakaoProfile = null;
        try {
            kakaoProfile = objectMapper.readValue(kakaoProfileResponse.getBody(), KakaoProfile.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return kakaoProfile;
    }


    public String getTokenByOauth(String token, String provider) {
        if (provider.equals("kakao")) {
            KakaoProfile profile = findKakaoProfile(token);

            //회원 정보 조회 by Email
            User user = userRepository.findByEmailAndProvider(profile.getKakao_account().getEmail(), provider);
            if (user == null) {
                user = User.builder()
                        .name(profile.getKakao_account().getProfile().getNickname())
                        .email(profile.getKakao_account().getEmail())
                        .provider(provider).build();
//                        .userRole("ROLE_USER").build();

                userRepository.save(user);
            } else if (provider.equals("email")) {

            } else {
                log.info("기존 회원 -> 회원 가입 건너 뜀");
            }
            return   tokenService.createToken(user);
        } else {
            //TODO: UnsupportedProviderExceptiob
            throw new RuntimeException("Unsupported provider: " + provider);
        }
    }



}
