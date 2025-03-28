package com.mallproject.UserService.mapper;


import com.mallproject.UserService.model.Token;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TokenMapper {
    Token findRefreshTokenByUserId(String userId);
    Token findRefreshTokenByToken(String token);
    int deleteRefreshToken(String token);
    int saveToken(Token token);
}
