package com.mallproject.UserService.serviceImpl;


import com.mallproject.UserService.mapper.TokenMapper;
import com.mallproject.UserService.model.Token;
import com.mallproject.UserService.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServicelmpl implements AuthService {
    private final TokenMapper tokenMapper;


    @Override
    public Token findRefreshTokenByUserId(String userId) {
        return tokenMapper.findRefreshTokenByUserId(userId);
    }

    @Override
    public Token findRefreshTokenByToken(String token) {
        return tokenMapper.findRefreshTokenByToken(token);
    }

    @Override
    public int deleteRefreshToken(String token) {
        return tokenMapper.deleteRefreshToken(token);
    }
}
