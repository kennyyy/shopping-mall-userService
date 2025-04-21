package com.mallproject.UserService.controller;

import com.mallproject.UserService.mapper.UserMapper;
import com.mallproject.UserService.model.Token;
import com.mallproject.UserService.model.User;
import com.mallproject.UserService.security.JWTService;
import com.mallproject.UserService.service.AuthService;
import com.mallproject.UserService.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Tag(name = "인증, 토큰 API")
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/user/auth")
public class AuthController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final AuthService authService;

    @Operation(summary = "토큰 재발급", description = "리프레쉬 토큰 검증 및 엑세스 토큰 재발급")
    @PostMapping("/reissue")
    public ResponseEntity<Token> refreshTokenVaild(@RequestBody Token token, @CookieValue(value = "refreshToken", required = false) String refreshToken){
        token.setRefreshToken(refreshToken);
        return authService.refreshTokenVaild(token);
    }

    @Operation(summary = "로그아웃", description = "리프레쉬 토큰 삭제")
    @PostMapping("/logout")
    public ResponseEntity<Token> logout(@RequestBody Token token, @CookieValue(value = "refreshToken", required = false) String refreshToken){
        token.setRefreshToken(refreshToken);
        return authService.logout(token);
    }


}
