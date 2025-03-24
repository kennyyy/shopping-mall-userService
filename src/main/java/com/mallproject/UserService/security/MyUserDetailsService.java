package com.mallproject.UserService.security;

import com.mallproject.UserService.mapper.UserMapper;
import com.mallproject.UserService.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

    private final UserMapper userMapper;

    MyUserDetailsService(UserMapper userMapper){
        this.userMapper = userMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //로그인 시도 할 시 이 메소드가 호출된다
        //매개변수로 들어온 user가 일치하는지만 확인한다
        //null 리턴시 AuthenticationFailureHandler 호출

        System.out.println(username);
        User user = userMapper.findUser(username);

        System.out.println("로그인");
        System.out.println("로그인 유저 : " + user);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);

        }

        return new MyUserDetails(user);
    }
}
