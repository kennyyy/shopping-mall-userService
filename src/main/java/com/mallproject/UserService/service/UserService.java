package com.mallproject.UserService.service;

import com.mallproject.UserService.model.User;
import org.springframework.http.ResponseEntity;
import java.util.List;


public interface UserService {
    List<User> getUserAll();
    ResponseEntity<User> findUser(String userId);
    void saveUser(User user);
}
