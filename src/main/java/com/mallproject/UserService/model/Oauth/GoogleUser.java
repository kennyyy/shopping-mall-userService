package com.mallproject.UserService.model.Oauth;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class GoogleUser implements MyOAuth2User {
    private final Collection<GrantedAuthority> authorities = new ArrayList<>();
    private Map<String, Object> attributes;

    public GoogleUser(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getName() {
        return (String)attributes.get("name");
    }

    @Override
    public String getProviderId() {
        return (String) attributes.get("sub");
    }

    @Override
    public String getProviderName() {
        return "google";
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }


}
