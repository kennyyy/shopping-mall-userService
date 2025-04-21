package com.mallproject.UserService.serviceImpl;


import com.auth0.jwt.JWT;
import com.mallproject.UserService.mapper.TokenMapper;
import com.mallproject.UserService.mapper.UserMapper;
import com.mallproject.UserService.model.Token;
import com.mallproject.UserService.model.User;
import com.mallproject.UserService.security.JWTService;
import com.mallproject.UserService.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServicelmpl implements AuthService {
    private final Logger log = LoggerFactory.getLogger(getClass());

    private final TokenMapper tokenMapper;
    private final UserMapper userMapper;
    private final JWTService jwtService;

    @Override
    public ResponseEntity<Token> refreshTokenVaild(Token token) {
        JWTService jwtService = new JWTService();
        String userId = jwtService.getClaimUserId(token.getRefreshToken());

        User user = userMapper.findUser(userId);
        Token refreshToken = tokenMapper.findRefreshTokenByToken(token.getRefreshToken());

        if(user == null || token.getRefreshToken().equals(refreshToken.getRefreshToken())
                || jwtService.isTokenValid(token.getRefreshToken())
                || jwtService.isExpiredCheck(token.getRefreshToken())){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        Token accessToken = Token.builder()
                .accessToken(jwtService.createAccessToken(user))
                .build();
        return new ResponseEntity<>(accessToken, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Token> logout(Token token) {
        //리프레쉬 토큰 삭제
        Token refreshToken = tokenMapper.findRefreshTokenByToken(token.getRefreshToken());
        if(refreshToken == null){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        tokenMapper.deleteRefreshToken(refreshToken.getRefreshToken());
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
