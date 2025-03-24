package com.mallproject.UserService.controller;

import com.mallproject.UserService.model.Token;
import com.mallproject.UserService.model.User;
import com.mallproject.UserService.security.JWTService;
import com.mallproject.UserService.service.AuthService;
import com.mallproject.UserService.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/auth")
public class AuthController {
    private final UserService userService;
    private final AuthService authService;

    @PostMapping("/access")
    public ResponseEntity<Token> accessTokenVaild(@RequestBody Token token){
        User user = userService.findUser(token.getUserId());
        Token refreshToken = authService.findRefreshTokenByToken(token.getRefreshToken());

        if(user == null || token.getRefreshToken().equals(refreshToken.getRefreshToken())
                        || JWTService.isTokenValid(token.getRefreshToken())
                        ||!JWTService.isExpiredCheck(token.getRefreshToken())){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }


        Token accessToken = Token.builder()
                .accessToken(JWTService.createToken(user))
                .build();
        return new ResponseEntity<>(accessToken, HttpStatus.OK);
    }



    @PostMapping("/logout")
    public ResponseEntity<Token> logout(@RequestBody Token token){
        //리프레쉬 토큰 삭제
        Token refreshToken = authService.findRefreshTokenByToken(token.getRefreshToken());
        if(refreshToken == null){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        authService.deleteRefreshToken(refreshToken.getRefreshToken());
        return new ResponseEntity<>(HttpStatus.OK);

    }


}
