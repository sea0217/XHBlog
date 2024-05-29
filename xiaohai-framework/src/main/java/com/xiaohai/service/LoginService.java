package com.xiaohai.service;

import com.xiaohai.domain.ResponseResult;
import com.xiaohai.domain.entity.User;

public interface LoginService {
    ResponseResult login(User user);

    ResponseResult logout();
}
