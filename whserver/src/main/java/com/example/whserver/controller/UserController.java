package com.example.whserver.controller;

import com.example.whserver.entity.Result;
import com.example.whserver.entity.User;
import com.example.whserver.page.Page;
import com.example.whserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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
}
