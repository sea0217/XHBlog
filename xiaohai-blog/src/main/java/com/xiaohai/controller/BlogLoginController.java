package com.xiaohai.controller;

import com.xiaohai.annotation.SystemLog;
import com.xiaohai.domain.ResponseResult;
import com.xiaohai.domain.entity.User;
import com.xiaohai.enums.AppHttpCodeEnum;
import com.xiaohai.exception.SystemException;
import com.xiaohai.service.BlogLoginService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "登录模块")
public class BlogLoginController {
    @Autowired
    private BlogLoginService blogLoginService;
    @PostMapping("/login")
    @SystemLog(businessName = "用户登录")
    @ApiOperation(value = "用户登录", notes = "用户登录")
    public ResponseResult login(@RequestBody User user) {
        if (!StringUtils.hasText(user.getUserName())) {
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
        return blogLoginService.login(user);
    }

    @PostMapping("/logout")
    @SystemLog(businessName = "用户退出登录")
    @ApiOperation(value = "用户退出登录", notes = "用户退出登录")
    public ResponseResult logout() {
        return blogLoginService.logout();
    }
}
