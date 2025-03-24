package com.mallproject.UserService.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.mallproject.UserService.mapper.UserMapper;
import com.mallproject.UserService.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.security.KeyStore;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class JWTService {

    @Autowired
    private UserMapper userMapper;

    @Value("${jwt.secretKey}")
    private String secretKey;

    private static String SECRET_KEY; // static 필드
    private static long REFRESHCHECKTIME = 1000 * 60 * 60 * 3;
    @PostConstruct
    public void init() {
        SECRET_KEY = secretKey;
    }

    public static String createToken(User user){
        long expireTime = System.currentTimeMillis() + 3600000;

        Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);

        return JWT.create()
                .withSubject(user.getUserName())
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(expireTime))
                .withIssuer("shoppingMSA")
                .withClaim("username", user.getUserId())
                .sign(algorithm);
    }
    public static String CreateRefreshToken(User user){
        long expireTime = System.currentTimeMillis() + 604800000;

        Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);

        return JWT.create()
                .withSubject(user.getUserName())
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(expireTime))
                .withIssuer("shoppingMSA")
                .withClaim("username", user.getUserId())
                .sign(algorithm);
    }


    public static boolean isTokenValid(String token) {
        try {
            JWT.require(Algorithm.HMAC256(SECRET_KEY)).build().verify(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    public static String getClaimUserName(String token) {
        DecodedJWT jwt = JWT.require(Algorithm.HMAC256(SECRET_KEY))
                .build()
                .verify(token);

        return jwt.getClaim("username").asString();
    }


    public static boolean isExpiredCheck(String token){
        DecodedJWT jwt = JWT.require(Algorithm.HMAC256(SECRET_KEY))
                .build()
                .verify(token);
        long now = System.currentTimeMillis();
        long expiredTime = jwt.getExpiresAt().getTime() - now;

        return expiredTime > REFRESHCHECKTIME;
    }

}
