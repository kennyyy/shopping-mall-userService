package com.mallproject.UserService.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.mallproject.UserService.mapper.UserMapper;
import com.mallproject.UserService.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;
import java.util.Date;



@Service
public class JWTService {

    @Value("${jwt.secretKey}")
    private String SECRET_KEY;

    @Value("${jwt.access-token.expiration-time}")
    private long ACCESSTIME;

    @Value("${jwt.refresh-token.expiration-time}")
    private long REFRESHTIME;

    //refresh 만료 5분전 미리 만료시키기 위한 time
    private final long REFRESHCHECKTIME = 1000 * 60 * 5;


    public String createAccessToken(User user){
        long expireTime = System.currentTimeMillis() + ACCESSTIME;

        Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);

        return JWT.create()
                .withSubject(user.getUserName())
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(expireTime))
                .withIssuer("shoppingMSA")
                .withClaim("userId", user.getUserId())
                .withClaim("userRole", user.getRole())
                .sign(algorithm);
    }

    public String CreateRefreshToken(User user){
        long expireTime = System.currentTimeMillis() + REFRESHTIME;

        Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);

        return JWT.create()
                .withSubject(user.getUserName())
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(expireTime))
                .withIssuer("shoppingMSA")
                .withClaim("username", user.getUserId())
                .withClaim("userRole", user.getRole())
                .sign(algorithm);
    }

    public boolean isTokenValid(String token) {
        try {
            JWT.require(Algorithm.HMAC256(SECRET_KEY)).build().verify(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }


    public String getClaimUserId(String token) {
        DecodedJWT jwt = JWT.require(Algorithm.HMAC256(SECRET_KEY))
                .build()
                .verify(token);

        return jwt.getClaim("userId").asString();
    }

    public String getClaimUserRole(String token) {
        DecodedJWT jwt = JWT.require(Algorithm.HMAC256(SECRET_KEY))
                .build()
                .verify(token);

        return jwt.getClaim("userRole").asString();
    }

    public boolean isExpiredCheck(String token){
        DecodedJWT jwt = JWT.require(Algorithm.HMAC256(SECRET_KEY))
                .build()
                .verify(token);
        long now = System.currentTimeMillis();
        long remainTime = jwt.getExpiresAt().getTime() - now;

        return remainTime < REFRESHCHECKTIME;
    }

}
