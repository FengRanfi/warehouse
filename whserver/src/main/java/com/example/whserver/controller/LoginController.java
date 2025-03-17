package com.example.whserver.controller;

import com.example.whserver.entity.*;
import com.example.whserver.service.AuthService;
import com.example.whserver.service.UserService;
import com.example.whserver.utils.DigestUtil;
import com.example.whserver.utils.TokenUtils;
import com.example.whserver.utils.WarehouseConstants;
import com.google.code.kaptcha.Producer;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;


@RestController
public class LoginController {
    //注入DefaultKatcha的bean对象--生成验证码图片的
    @Resource(name="captchaProducer")
    private Producer producer;
    //注入redis模版
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    private WarehouseConstants warehouseConstants;
    @Autowired
    private TokenUtils tokenUtils;

    @RequestMapping("/captcha/captchaImage")
    public void captchaImage(HttpServletResponse response){
        ServletOutputStream out = null;
        try
        {//生成验证码图片文件
            String text = producer.createText();
            //使用验证码文本生成图片 BufferedImage代表图片在内存里
            BufferedImage image = producer.createImage(text);
            //将验证码文件保存到redis,设置过期时间为三十分钟
            stringRedisTemplate.opsForValue().set(text, "", 1800, TimeUnit.SECONDS);
            //将验证码响应给前端
            response.setContentType("image/jpeg");
            //写给前端
            out = response.getOutputStream();
            ImageIO.write(image, "jpg", out);
            //刷新
            out.flush();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        finally {
            //关闭字节输出流
            if(out!=null)
            {
                try
                {
                    out.close();
                }
                catch(IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
    /*
    登录的url接H/login
    参数@RequestBody Loginuser loginuser -- 表示接收并封装前端传递的登录的用户信息的json数据;
    返回值Result对象 --表示向前端响应响应结果Result对象转的ison串,包含响应状态码 成功失败响应 响应信息 响应数据;
     */
    @Autowired
    private UserService userService;
    @Autowired
    private TokenUtils tokenUtil;
    @RequestMapping("/login")
    public Result login(@RequestBody LoginUser loginUser)
    {
        String code=loginUser.getVerificationCode();
        if(!stringRedisTemplate.hasKey(code))
        {
            return Result.err(Result.CODE_ERR_BUSINESS,"验证码错误!");
        }
        User user=userService.queryUserByCode(loginUser.getUserCode());
        if(user!=null)
        {
            if(user.getUserState().equals(WarehouseConstants.USER_STATE_PASS))
            {
                String userPwd=loginUser.getUserPwd();
                userPwd=DigestUtil.hmacSign(userPwd);
                if(userPwd.equals(user.getUserPwd()))
                {
                    CurrentUser currentUser=new CurrentUser(user.getUserId(),user.getUserCode(),user.getUserName());
                    String token=tokenUtils.loginSign(currentUser,userPwd);
                    return Result.ok("登陆成功",token);
                }
                else
                {
                    return Result.err(Result.CODE_ERR_BUSINESS,"密码错误");
                }
            }
            else
            {
                return Result.err(Result.CODE_ERR_BUSINESS,"用户未审核");
            }
        }
        else
        {
            return Result.err(Result.CODE_ERR_BUSINESS,"账号不存在");
        }
    }

    @RequestMapping("/curr-user")
    public Result currentUser(@RequestHeader(WarehouseConstants.HEADER_TOKEN_NAME) String token)
    {
        CurrentUser currentUser=tokenUtils.getCurrentUser(token);
        return Result.ok(currentUser);
    }
    @Autowired
    private AuthService authService;
    @RequestMapping("/user/auth-list")
    public Result loadAuthTree(@RequestHeader(WarehouseConstants.HEADER_TOKEN_NAME) String token)
    {
        CurrentUser currentUser=tokenUtils.getCurrentUser(token);
        int userId =currentUser.getUserId();
        List<Auth> authTreeList=authService.authTreeByUid(userId);
        return Result.ok(authTreeList);
    }
}
