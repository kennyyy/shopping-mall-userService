package com.mallproject.UserService.controller;

import com.mallproject.UserService.model.User;
import com.mallproject.UserService.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/user")
public class UserController {
    //SLf4j
    private final Logger log = LoggerFactory.getLogger(getClass());

    private final UserService userService;

    @Autowired
    UserController(UserService userService){
        this.userService=userService;
    }

    @RequestMapping("/login")
    public ResponseEntity<List<User>> getUsers(){

        HttpHeaders headers = new HttpHeaders(); //헤더

        headers.add("Access-Control-Allow-Origin", "true");
        log.info("사용자 조회 : ${}" , userService.getUserInfo());
        return new ResponseEntity<>(userService.getUserInfo(), headers, HttpStatus.OK);
    }

}
