package com.swave.urnr.util.oauth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Component
@Slf4j
public class JwtRequestFilter extends OncePerRequestFilter {



    // 인증에서 제외할 url
    private static final String Exclude_url="/api/test,/api/project/**,/personal/**,/api/user/create,/api/user/login,/api/user/sendmail,/api/user/temp/**," +
                    "/api/user/prelogin/**,/swagger/**,/v2/api-docs/**,/configuration/ui/**," +
                    "/swagger-resources/**,/configuration/security/**," +
                    "/swagger-ui/**,/webjars/**,/swagger-ui.html"+
                    ",/v2/api-docs,/favicon.ico,/";

    private static final List<String> EXCLUDE_URL =
            Collections.unmodifiableList(
                    Arrays.asList(
                            "/api/user/oauth/token"
                    ));
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwtHeader = ((HttpServletRequest)request).getHeader(JwtProperties.HEADER_STRING);
        log.info(request.getRequestURI());
        String[] excludeUrls = Exclude_url.split(",");
        for (String excludeUrl : excludeUrls) {
            log.info(excludeUrl+" "+request.getRequestURI());
        }

        if (pathMatchesExcludePattern(request.getRequestURI())) {
            // Skip JWT authentication for excluded URLs
            try {
                filterChain.doFilter(request, response);
            }catch(Exception e)
            {
                log.info(e.toString());
            }
            return;
        }
        else if(request.getHeader("Authorization") == null) {
            request.setAttribute(JwtProperties.HEADER_STRING, "Authorization이 없습니다.");
            log.info("Authorization needed");
            throw new ServletException();
        }
        // header 가 정상적인 형식인지 확인
        if(jwtHeader == null || !jwtHeader.startsWith(JwtProperties.TOKEN_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }

        // jwt 토큰을 검증해서 정상적인 사용자인지 확인
        String token = jwtHeader.replace(JwtProperties.TOKEN_PREFIX, "");

        Long id = null;

        if (pathMatchesExcludePattern(request.getRequestURI())) {
            // Skip JWT authentication for excluded URLs
            filterChain.doFilter(request, response);
            return;
        }
        else {
            try {
                id = JWT.require(Algorithm.HMAC512(JwtProperties.SECRET)).build().verify(token).getClaim("id").asLong();
                System.out.println(id);
            } catch (TokenExpiredException e) {
                e.printStackTrace();
                request.setAttribute(JwtProperties.HEADER_STRING, "토큰이 만료되었습니다.");
                System.out.println("토큰 만료");
                throw new ServletException();
            } catch (JWTVerificationException e) {
                e.printStackTrace();
                request.setAttribute(JwtProperties.HEADER_STRING, "유효하지 않은 토큰입니다.");
                System.out.println("유효x");
                throw new ServletException();
            }
        }

/*
Request has now have attribute value
 */
        request.setAttribute("id", id);
        filterChain.doFilter(request, response);
    }
    // Filter에서 제외할 URL 설정
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return EXCLUDE_URL.stream().anyMatch(exclude -> exclude.equalsIgnoreCase(request.getServletPath()));
    }
    private boolean pathMatchesExcludePattern(String requestURI) {
        AntPathMatcher pathMatcher = new AntPathMatcher();
        String[] excludeUrls = Exclude_url.split(",");

        log.info(excludeUrls.toString());

        for (String excludeUrl : excludeUrls) {
            if (pathMatcher.match(excludeUrl, requestURI)) {
                return true;
            }
        }
        return false;
    }

}