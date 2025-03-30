package com.example.whserver.mapper;

import com.example.whserver.entity.User;
import com.example.whserver.page.Page;
import io.lettuce.core.dynamic.annotation.Param;

import java.util.List;

/*
user_info 表的mapper 接口:
*/
public interface UserMapper {
    //根据账号查询用户信息的方法
    public User findUserByCode(String userCode);
    //查询用户总行数
    public Integer findUserRowCount(User user);
    //分页查询用户的方法
    public List<User> findUserByPage(@Param("page")Page page,@Param("user")User user);
}
