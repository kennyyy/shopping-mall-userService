package com.mallproject.UserService.service;

import com.mallproject.UserService.model.User;
import org.springframework.stereotype.Service;

import java.util.List;


public interface UserService {
    List<User> getUserAll();
    User findUser(String userId);
    void saveUser(User user);
}
