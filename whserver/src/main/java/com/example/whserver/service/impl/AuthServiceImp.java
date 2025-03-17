package com.example.whserver.service.impl;

import com.alibaba.fastjson.JSON;
import com.example.whserver.dto.AssignAuthDto;
import com.example.whserver.entity.Auth;
import com.example.whserver.mapper.AuthMapper;
import com.example.whserver. service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class AuthServiceImp implements AuthService {

	@Autowired
	private AuthMapper authMapper;
	@Autowired
	private StringRedisTemplate stringRedisTemplate;
    @Qualifier("redisTemplate")
    @Autowired
    private RedisTemplate redisTemplate;

	@Override
	public List<Auth> authTreeByUid(Integer userId) {
		//get info from redis
		String authTreeJson= (String) redisTemplate.opsForValue().get("authTree:"+userId);
		if (StringUtils.hasText(authTreeJson))
		{
			List<Auth> authTreeList=JSON.parseArray(authTreeJson,Auth.class);
			return authTreeList;
		}
		List<Auth>allAuthList=authMapper.findAuthByUid(userId);
		List<Auth>authTreeList=allAuthToAuthTree(allAuthList,0);
		redisTemplate.opsForValue().set("authTree:"+userId,JSON.toJSONString(authTreeList));
		return authTreeList;
	}
	private List<Auth> allAuthToAuthTree(List<Auth> allAuthList,Integer pid){
		List<Auth> firstLevelAuthList=new ArrayList<>();
		for (Auth auth:allAuthList){
			if(auth.getParentId()==pid)
			{
				firstLevelAuthList.add(auth);
			};
		}
		for (Auth firstAuth:firstLevelAuthList){
			List<Auth>secondLevelAuthList=allAuthToAuthTree(allAuthList,firstAuth.getAuthId());
			firstAuth.setChildAuth(secondLevelAuthList);
		}
		return firstLevelAuthList;
	}
}
