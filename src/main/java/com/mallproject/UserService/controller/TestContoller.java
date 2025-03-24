package com.mallproject.UserService.controller;

import org.springframework.web.bind.annotation.*;

@RestController
public class TestContoller {




    @GetMapping("/home")
    public String test03(){
        return "hi";
    }
}
