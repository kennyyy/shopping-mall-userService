package com.mallproject.UserService.mapper;


import com.mallproject.UserService.model.Token;
import com.mallproject.UserService.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface TokenMapper {
    Token findRefreshTokenByUserId(String userId);
    Token findRefreshTokenByToken(String token);
    int deleteRefreshToken(String token);
    int saveToken(Token token);
}
