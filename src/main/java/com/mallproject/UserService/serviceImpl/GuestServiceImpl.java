package com.mallproject.UserService.serviceImpl;

import com.mallproject.UserService.mapper.UserMapper;
import com.mallproject.UserService.model.User;
import com.mallproject.UserService.service.GuestService;
import com.mallproject.UserService.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class GuestServiceImpl implements GuestService {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final UserMapper userMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public ResponseEntity<User> userJoin(User user) {
        User DBUser = userMapper.findUser(user.getUserId());
        log.info("고객 가입 데이터 : " + user);

        if(DBUser == null){
            user.setUserPw(bCryptPasswordEncoder.encode(user.getUserPw()));
            user.setUserProvider("shopping-mall");
            userMapper.saveUser(user);
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
        return new ResponseEntity<>(user, HttpStatus.FORBIDDEN);
    }
}
