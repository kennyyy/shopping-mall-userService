package com.mallproject.UserService.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;
import java.util.List;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User{
    private String userId;
    private String userPw;
    private String userProvider;
    private String userName;
    private String userEmail;
    private String userPhone;
    private int userAge;
    private String userGender;
    private String userLocation;
    private String role;
    
    //권한List
    List<GrantedAuthority> authorities = new ArrayList<>();

}
