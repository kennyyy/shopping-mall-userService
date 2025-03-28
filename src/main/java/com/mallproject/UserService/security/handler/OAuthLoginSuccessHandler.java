package com.mallproject.UserService.security.handler;


import com.mallproject.UserService.mapper.TokenMapper;
import com.mallproject.UserService.mapper.UserMapper;
import com.mallproject.UserService.model.Oauth.GoogleUser;
import com.mallproject.UserService.model.Oauth.MyOAuth2User;
import com.mallproject.UserService.model.Oauth.NaverUser;
import com.mallproject.UserService.model.Token;
import com.mallproject.UserService.model.User;
import com.mallproject.UserService.security.JWTService;
import com.mallproject.UserService.security.MyUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Map;


@Component
@RequiredArgsConstructor
public class OAuthLoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    @Value("${jwt.refresh-token.expiration-time}")
    private int refreshExpireTime;

    @Value("${jwt.redirect-uri}")
    private String redirectURL;
    private MyOAuth2User myOAuth2User;
    private final UserMapper userMapper;
    private final TokenMapper tokenMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
        String provider = token.getAuthorizedClientRegistrationId();

        if(provider.equals("google")){
            myOAuth2User = new GoogleUser(token.getPrincipal().getAttributes());
        } else if (provider.equals("naver")) {
            myOAuth2User = new NaverUser((Map<String, Object>) token.getPrincipal().getAttributes().get("response"));
        }
        String username = myOAuth2User.getName();
        String providerId = myOAuth2User.getProviderId();

        User DBUser = userMapper.findUser(providerId);

        if(DBUser == null){
            DBUser = User.builder()
                    .userId(providerId)
                    .userName(username)
                    .userProvider(myOAuth2User.getProviderName())
                    .userEmail(myOAuth2User.getEmail())
                    .role("USER")
                    .build();
            userMapper.saveUser(DBUser);
        }

        String accessToken = JWTService.createAccessToken(DBUser);
        String refreshToken = JWTService.CreateRefreshToken(DBUser);

        Token saveToken = Token.builder()
                .userId(providerId)
                .refreshToken(refreshToken)
                .build();
        tokenMapper.saveToken(saveToken);

        response.setContentType("application/json; charset=UTF8;");
        response.addHeader("Authorization", "Bearer " + accessToken);

        Cookie cookie = new Cookie("refreshToken", refreshToken);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setMaxAge(refreshExpireTime);
        response.addCookie(cookie);

        response.sendRedirect(redirectURL);
    }
}
