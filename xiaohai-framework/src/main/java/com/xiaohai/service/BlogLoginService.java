package com.xiaohai.service;

import com.xiaohai.domain.ResponseResult;
import com.xiaohai.domain.entity.User;

public interface BlogLoginService {
    ResponseResult login(User user);

    ResponseResult logout();
}
