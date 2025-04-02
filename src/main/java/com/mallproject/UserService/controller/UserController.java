package com.mallproject.UserService.controller;

import com.mallproject.UserService.model.User;
import com.mallproject.UserService.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "유저 API")
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/user/info")
public class UserController {
    //SLf4j
    private final Logger log = LoggerFactory.getLogger(getClass());

    private final UserService userService;
    
    @Operation(summary = "유저 조회", description = "ID로 유저 정보를 조회")
    @GetMapping("/find/{userId}")
    public ResponseEntity<User> getUsers(@PathVariable  String userId){
        return userService.findUser(userId);
    }


}
