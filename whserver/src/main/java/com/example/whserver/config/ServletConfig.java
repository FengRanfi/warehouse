package com.example.whserver.config;


import com.example.whserver.filter.LoginCheckFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

@Configuration
public class ServletConfig {
    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Bean
    public FilterRegistrationBean filterRegistrationBean()
    {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        LoginCheckFilter loginCheckFilter = new LoginCheckFilter();
        loginCheckFilter.setStringRedisTemplate(stringRedisTemplate);
        filterRegistrationBean.setFilter(loginCheckFilter);
        filterRegistrationBean.addUrlPatterns("/*");
        return filterRegistrationBean;
    }
}
