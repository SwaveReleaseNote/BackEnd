package com.swave.urnr.util.oauth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.CrossOrigin;
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
@CrossOrigin(origins = "http://61.109.214.110:80", allowedHeaders = "*", allowCredentials = "true")
public class JwtRequestFilter extends OncePerRequestFilter {



    // 인증에서 제외할 url
    // 3번째줄 부터는 swagger
    private static final String Exclude_url="/api/test, /actuator/**," +
            "/swagger/**,/v2/api-docs/**,/configuration/ui/**," +
            "/swagger-resources/**,/configuration/security/**," +
            "/swagger-ui/**,/webjars/**,/swagger-ui.html," +
            "/api/subscribe/**," + "/api/publish/**,"+
            "/api/kafka/**,"+"/api/user/sample,/api/user/login**,"+
            "/api/sse/**,"+ "/favicon.ico**,"+
            "/swagger-ui/**,/webjars/**,/swagger-ui.html,"+"/ws-stomp/**";



    private static final String Exclude_post_url="/api/user,"+
            "/api/user/validation,"+
            "/api/user/temporary-password,"+
            "/api/user/login**";

    private static final List<String> EXCLUDE_URL =
            Collections.unmodifiableList(
                    Arrays.asList(
                            "/api/user/prelogin/login-by-oauth",
                            "/actuator/prometheus"
                    ));
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwtHeader = ((HttpServletRequest)request).getHeader(JwtProperties.HEADER_STRING);

        if (pathMatchesExcludePattern(request)) {
            // Skip JWT authentication for excluded URLs
            try {
                filterChain.doFilter(request, response);
            }catch(RuntimeException e)
            {
                log.info(" FAILED for "+ request.getRequestURI());
                log.info(e.toString());
            }
            return;
        }
        else if(request.getHeader("Authorization") == null) {
            log.info("error: No Authorization token on header for URI " + request.getRequestURI());
            request.setAttribute(JwtProperties.HEADER_STRING, "Authorization 이 없습니다.");
            throw new RuntimeException("No authorization.  token name were " +request.getRequestURI());
        }
        // header 가 정상적인 형식인지 확인
        if(jwtHeader == null || !jwtHeader.startsWith(JwtProperties.TOKEN_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }

        // jwt 토큰을 검증해서 정상적인 사용자인지 확인
        String token = jwtHeader.replace(JwtProperties.TOKEN_PREFIX, "");

        Long id = null;
        String name = null;

        if (pathMatchesExcludePattern(request)) {
            // Skip JWT authentication for excluded URLs
            filterChain.doFilter(request, response);
            return;
        }
        else {
            try {
                id = JWT.require(Algorithm.HMAC512(JwtProperties.SECRET)).build().verify(token).getClaim("id").asLong();
                name = JWT.require(Algorithm.HMAC512(JwtProperties.SECRET)).build().verify(token).getClaim("name").asString();
                log.info(id.toString());
            } catch (TokenExpiredException e) {
                e.printStackTrace();
                request.setAttribute(JwtProperties.HEADER_STRING, "토큰이 만료되었습니다.");
                log.info("만료된 토큰");

                throw new ServletException();

            } catch (JWTVerificationException e) {
                e.printStackTrace();
                request.setAttribute(JwtProperties.HEADER_STRING, "유효하지 않은 토큰입니다.");
                log.info("유효하지 않은 토큰");

                throw new ServletException();
            }
        }

/*
Request has now have attribute value
 */
        request.setAttribute("id", id);
        request.setAttribute("name", name);
        filterChain.doFilter(request, response);
    }
    // Filter에서 제외할 URL 설정
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return EXCLUDE_URL.stream().anyMatch(exclude -> exclude.equalsIgnoreCase(request.getServletPath()));
    }
    private boolean pathMatchesExcludePattern(HttpServletRequest request ) {
        String requestURI = request.getRequestURI();

        AntPathMatcher pathMatcher = new AntPathMatcher();
        String[] excludeUrls = Exclude_url.split(",");



        Boolean tempSwitch = false;


        for (String excludeUrl : excludeUrls) {
//            log.info("URLS : "+excludeUrl.toString() +" SO R : "+ pathMatcher.match(excludeUrl,requestURI));
            if (pathMatcher.match(excludeUrl, requestURI)) {
//                log.info("Out of loop");
                tempSwitch = true;
                return true;

            }
        }

        if(tempSwitch ||request.getMethod().toString().equals("POST")){

            String[] excludePostUrls = Exclude_post_url.split(",");
            for (String excludeUrl : excludePostUrls ) {

                if (pathMatcher.match(excludeUrl, requestURI)) {

                    return true;
                }
            }
        }
        return false;
    }

}
