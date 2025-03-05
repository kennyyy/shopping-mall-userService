package com.mallproject.UserService.mapper;

import com.mallproject.UserService.model.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {
    List<User> getUserInfo();
}
