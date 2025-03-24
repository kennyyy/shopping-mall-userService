package com.mallproject.UserService.controller;


import com.mallproject.UserService.model.User;
import com.mallproject.UserService.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/guest")
public class GuestController {
    //SLf4j
    private final Logger log = LoggerFactory.getLogger(getClass());

    private final UserService userService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @RequestMapping("/join")
    public ResponseEntity<User> joinUser(@RequestBody User user){
        User DBUser = userService.findUser(user.getUserId());
        System.out.println(user);
        if(DBUser == null){
            user.setUserPw(bCryptPasswordEncoder.encode(user.getUserPw()));
            user.setUserProvider("shopping-mall");
            userService.saveUser(user);
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
        return new ResponseEntity<>(user, HttpStatus.FORBIDDEN);
    }
}
