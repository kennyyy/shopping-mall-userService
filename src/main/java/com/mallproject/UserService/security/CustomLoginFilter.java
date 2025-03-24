package com.mallproject.UserService.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mallproject.UserService.mapper.TokenMapper;
import com.mallproject.UserService.model.Token;
import com.mallproject.UserService.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Service;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;



public class CustomLoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final TokenMapper tokenMapper;


    public CustomLoginFilter(AuthenticationManager authenticationManager, TokenMapper tokenMapper) {
        this.authenticationManager = authenticationManager;
        this.tokenMapper = tokenMapper;
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        //json일시 json -> MAP
        if(request.getHeader("Content-type").equals("application/json")){
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                Map<String, String> requestBody = objectMapper.readValue(request.getInputStream(), Map.class);
                username = requestBody.get("username");
                password = requestBody.get("password");
                System.out.println(username + " , " + password);
            }catch (IOException e) {
                throw new RuntimeException("Failed to parse authentication request body", e);
            }
        }

        //가져온 데이터로 token화 시키고
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);

        //토큰을 이용하여 인증수행
        Authentication authentication = authenticationManager.authenticate(token);

        return authentication;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {


        MyUserDetails principal = (MyUserDetails)authResult.getPrincipal();


        //JWT 토큰 생성 후 header에 담기
        String accessToken = JWTService.createToken(principal.getUser());
        String refreshToken = JWTService.CreateRefreshToken(principal.getUser());

        Token saveToken = Token.builder()
                .userId(principal.getUser().getUserId())
                .refreshToken(refreshToken)
                .build();
        tokenMapper.saveToken(saveToken);


        response.setContentType("application/json; charset=UTF8;");
        response.addHeader("Authorization", "Bearer " + accessToken);

        Cookie cookie = new Cookie("refreshToken", refreshToken);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setMaxAge(604800000);
        response.addCookie(cookie);


        response.getWriter().println("로그인성공(아이디):" + accessToken );


    }


    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {

        response.setContentType("application/json; charset=UTF8");
        response.sendError(500, "로그인 실패. 아이디 비밀번호를 확인하세요.");

    }
}
