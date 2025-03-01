package com.example.whserver.mapper;

import com.example.whserver.entity.User;

/*
user_info 表的mapper 接口:
*/
public interface UserMapper {
    //根据账号查询用户信息的方法
    public User findUserByCode(String userCode);
}
