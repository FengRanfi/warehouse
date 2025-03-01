package com.example.whserver.service.impl;

import com.example.whserver.entity.User;
import com.example.whserver.mapper.UserMapper;
import com.example.whserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    // 注入UserMapper
    @Autowired
    private UserMapper userMapper;

    // 根据账号查询用户的业务方法
    @Override
    public User queryUserByCode(String userCode) {
        return userMapper.findUserByCode(userCode);
    }
}
