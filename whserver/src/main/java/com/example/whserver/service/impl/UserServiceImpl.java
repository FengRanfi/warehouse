package com.example.whserver.service.impl;

import com.example.whserver.entity.User;
import com.example.whserver.mapper.UserMapper;
import com.example.whserver.page.Page;
import com.example.whserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Override
    public Page queryUserByPage(Page page, User user) {
        //查询用户行数
        Integer count= userMapper.findUserRowCount(user);
        //分页查询用户
        List<User> userList=userMapper.findUserByPage(page,user);
        page.setTotalNum(count);
        page.setResultList(userList);
        return page;
    }
}
