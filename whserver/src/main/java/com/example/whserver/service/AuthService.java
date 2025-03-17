package com.example.whserver.service;

import com.example.whserver.dto.AssignAuthDto;
import com.example.whserver.entity.Auth;
import com.example.whserver.mapper.AuthMapper;

import java.util.List;

public interface AuthService {

	//查询用户菜单树
	public List<Auth> authTreeByUid(Integer userId);

}
