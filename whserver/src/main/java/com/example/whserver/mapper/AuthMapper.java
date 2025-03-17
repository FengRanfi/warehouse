package com.example.whserver.mapper;

import com.example.whserver.entity.Auth;

import java.util.List;

public interface AuthMapper {
    public List<Auth> findAuthByUid(Integer userid);
}
