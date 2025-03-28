package com.mallproject.UserService.service;

import com.mallproject.UserService.model.Token;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

public interface AuthService {
    ResponseEntity<Token> refreshTokenVaild(Token token);
    ResponseEntity<Token> logout(@RequestBody Token token);
}
