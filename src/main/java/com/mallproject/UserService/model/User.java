package com.mallproject.UserService.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private String userId;
    private String userPw;
    private String userNm;
    private String userEmail;
    private String userPhone;
    private int userAge;
    private String userGen;
    private String userLocation;
    private int userFailCNT;
}
