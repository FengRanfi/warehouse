package com.example.whserver.filter;

import com.alibaba.fastjson.JSON;
import com.example.whserver.entity.Result;
import com.example.whserver.utils.WarehouseConstants;
import io.netty.util.internal.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class LoginCheckFilter implements Filter {
    private StringRedisTemplate stringRedisTemplate;

    public void setStringRedisTemplate(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        //白名单
        List<String> urlList=new ArrayList<>();
        urlList.add("/captcha/captchaImage");
        urlList.add("/login");
        String url=request.getServletPath();
        if(urlList.contains(url))
        {
            chain.doFilter(req, resp);
            return;
        }
        //其它请求，验证token
        String token=request.getHeader(WarehouseConstants.HEADER_TOKEN_NAME);
        if(StringUtils.hasText(token)&&stringRedisTemplate.hasKey(token))
        {
            chain.doFilter(req, resp);
            return;
        }

        Result res=Result.err(Result.CODE_ERR_UNLOGINED,"你尚未登录");
        String jsonStr= JSON.toJSONString(res);
        response.setContentType("application/json;charset=utf-8");
        PrintWriter out = response.getWriter();
        out.write(jsonStr);
        out.flush();
        out.close();
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
