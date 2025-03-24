package com.mallproject.UserService.service;

import com.mallproject.UserService.model.Token;

public interface AuthService {
    Token findRefreshTokenByUserId(String userId);
    Token findRefreshTokenByToken(String token);
    int deleteRefreshToken(String token);
}
