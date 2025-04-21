package com.mallproject.UserService.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mallproject.UserService.mapper.TokenMapper;
import com.mallproject.UserService.model.Token;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
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

    private final TokenMapper tokenMapper;
    private final JWTService jwtService;

    @Value("${jwt.refresh-token.expiration-time}")
    private int refreshExpireTime;

    @Autowired
    public CustomLoginFilter(AuthenticationManager authenticationManager, TokenMapper tokenMapper, JWTService jwtService) {
        super(authenticationManager);
        this.tokenMapper = tokenMapper;
        this.jwtService = jwtService;
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
        Authentication authentication = getAuthenticationManager().authenticate(token);

        return authentication;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {


        MyUserDetails principal = (MyUserDetails)authResult.getPrincipal();


        //엑세스 토큰 발행
        String accessToken = jwtService.createAccessToken(principal.getUser());

        //리프레쉬 토큰 발행
        //추후 https 설정하면 진행할 예정
//        String refreshToken = jwtService.CreateRefreshToken(principal.getUser());
//
//        Token saveToken = Token.builder()
//                .userId(principal.getUser().getUserId())
//                .refreshToken(refreshToken)
//                .build();
//        tokenMapper.saveToken(saveToken);
        
        //Cookie cookie = new Cookie("refreshToken", refreshToken);
        //cookie.setHttpOnly(true);
        //cookie.setSecure(true);
        //cookie.setMaxAge(refreshExpireTime);
        //cookie.setPath("/");
        //response.addCookie(cookie);

        response.setContentType("application/json; charset=UTF8;");
        response.addHeader("Authorization", "Bearer " + accessToken);
        response.getWriter().println("로그인성공:");
    }


    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {

        response.setContentType("application/json; charset=UTF8");
        response.sendError(500, "로그인 실패. 아이디 비밀번호를 확인하세요.");

    }
}
