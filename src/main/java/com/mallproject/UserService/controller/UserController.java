package com.mallproject.UserService.controller;

import com.mallproject.UserService.model.User;
import com.mallproject.UserService.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "유저 API")
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/user")
public class UserController {
    //SLf4j
    private final Logger log = LoggerFactory.getLogger(getClass());

    private final UserService userService;

    @Value("${server.env}")
    private String env;

    @GetMapping("/env")
    public ResponseEntity<String> getEnv(){
        return ResponseEntity.ok(env);
    }

    @Operation(summary = "유저 조회", description = "ID로 유저 정보를 조회")
    @GetMapping("/info/find/{userId}")
    public ResponseEntity<User> getUsers(@PathVariable  String userId){
        return userService.findUser(userId);
    }


}
