package com.mallproject.UserService.service;

import com.mallproject.UserService.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

public interface GuestService {
    ResponseEntity<User> userJoin(@RequestBody User user);
}
