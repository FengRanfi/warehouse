package com.example.whserver.controller;

import com.example.whserver.entity.CurrentUser;
import com.example.whserver.entity.Result;
import com.example.whserver.entity.User;
import com.example.whserver.page.Page;
import com.example.whserver.service.UserService;
import com.example.whserver.utils.TokenUtils;
import com.example.whserver.utils.WarehouseConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RequestMapping("/user")
@RestController
public class UserController {
    @Autowired
    private UserService userService;
    @RequestMapping("/user-list")
    public Result userList(Page page, User user)
    {
        page=userService.queryUserByPage(page,user);
        return Result.ok(page);
    }
    @Autowired
    private TokenUtils tokenUtils;
    @RequestMapping("/addUser")
    public Result add(@RequestBody User user,@RequestHeader(WarehouseConstants.HEADER_TOKEN_NAME) String token)
    {
        CurrentUser currentUser=tokenUtils.getCurrentUser(token);
        int createBy=currentUser.getUserId();
        user.setCreateBy(createBy);
        Result result=userService.saveUser(user);
        return result;
    }

    //修改用户状态/user/updateState
    @RequestMapping("/updateState")
    public Result updateUserState(@RequestBody User user)
    {
        Result result=userService.setUserState(user);
        return result;
    }
}
