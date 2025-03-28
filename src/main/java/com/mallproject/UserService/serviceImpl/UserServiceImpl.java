package com.mallproject.UserService.serviceImpl;

import com.mallproject.UserService.mapper.UserMapper;
import com.mallproject.UserService.model.User;
import com.mallproject.UserService.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final UserMapper userMapper;


    @Override
    public List<User> getUserAll() {
        return userMapper.getUserInfo();
    }

    @Override
    public ResponseEntity<User> findUser(String userId) {
        User findUser = userMapper.findUser(userId);

        if(findUser == null){
            new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Access-Control-Allow-Origin", "true");
        logger.info("사용자 조회 : ${}" , findUser);

        return new ResponseEntity<>(findUser, headers, HttpStatus.OK);
    }

    @Override
    public void saveUser(User user) {
        userMapper.saveUser(user);
    }
}
