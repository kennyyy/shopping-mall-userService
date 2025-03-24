package com.mallproject.UserService.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Token {
    private int tokenId;
    private String userId;
    private String refreshToken;
    private String accessToken;
}
