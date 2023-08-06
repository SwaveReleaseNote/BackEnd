package com.swave.urnr.user.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.swave.urnr.util.oauth.JwtProperties;
import com.swave.urnr.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;

public interface TokenService {
    String createToken(User user);
}
