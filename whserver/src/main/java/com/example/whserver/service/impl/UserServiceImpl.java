package com.example.whserver.service.impl;

import com.example.whserver.entity.Result;
import com.example.whserver.entity.User;
import com.example.whserver.mapper.UserMapper;
import com.example.whserver.page.Page;
import com.example.whserver.service.UserService;
import com.example.whserver.utils.DigestUtil;
import com.example.whserver.utils.TokenUtils;
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

    @Override
    public Result saveUser(User user)
    {
        //判断账号是否已经存在
        User u=userMapper.findUserByCode(user.getUserCode());
        if(u!=null)
        {
            return Result.err(Result.CODE_ERR_BUSINESS,"账号已存在!");
        }
        //对密码做加密操作
        String password=DigestUtil.hmacSign(user.getUserPwd());
        user.setUserPwd(password);
        //执行添加
        int i=userMapper.insertUser(user);
        if(i>0)
        {
            return Result.ok("用户添加成功");
        }
        return Result.err(Result.CODE_ERR_BUSINESS,"用户添加失败");
    }
    @Override
    public Result setUserState(User user)
    {
        int i=userMapper.setStateByUid(user.getUserId(),user.getUserState());
        if(i>0)
        {
            return Result.ok("启用或禁用用户");
        }
        return Result.err(Result.CODE_ERR_BUSINESS,"启用或禁用用户失败");
    }
}
