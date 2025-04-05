package com.example.whserver.service;

import com.example.whserver.entity.Result;
import com.example.whserver.entity.User;
import com.example.whserver.page.Page;

/*
user_info表的service接口:
*/
public interface UserService {
    //根据账号查询用户的业务方法
    public User queryUserByCode(String userCode);
    //分页查询用户的业务方法
    public Page queryUserByPage(Page page,User user);
    //添加用户业务方法
    public Result saveUser(User user);
    //启用或禁用用户的业务方法
    public Result setUserState(User user);
}
