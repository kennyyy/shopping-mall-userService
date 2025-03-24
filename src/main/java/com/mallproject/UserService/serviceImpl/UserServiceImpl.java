package com.mallproject.UserService.serviceImpl;

import com.mallproject.UserService.mapper.UserMapper;
import com.mallproject.UserService.model.User;
import com.mallproject.UserService.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    @Autowired
    UserServiceImpl(UserMapper userMapper){
        this.userMapper = userMapper;
    }


    @Override
    public List<User> getUserAll() {
        return userMapper.getUserInfo();
    }

    @Override
    public User findUser(String userId) {
        return userMapper.findUser(userId);
    }

    @Override
    public void saveUser(User user) {
        userMapper.saveUser(user);
    }
}
