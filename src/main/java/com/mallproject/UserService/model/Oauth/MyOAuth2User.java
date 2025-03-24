package com.mallproject.UserService.model.Oauth;

import org.springframework.security.oauth2.core.user.OAuth2User;

//인증서버에서 받은 유저정보를 DB저장에 필요한 자료로 추출하기 위한 interface
public interface MyOAuth2User extends OAuth2User {

    String getProviderId();
    String getProviderName();
    String getEmail();
}
