package com.xiaohai.controller;

import com.xiaohai.annotation.SystemLog;
import com.xiaohai.domain.ResponseResult;
import com.xiaohai.domain.dto.UserInfoDto;
import com.xiaohai.domain.entity.User;
import com.xiaohai.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@Api(tags = "用户模块")
public class UserController {
    @Autowired
    private UserService userService;
    @GetMapping("userInfo")
    @SystemLog(businessName = "获取用户信息")
    @ApiOperation("获取用户信息")
    public ResponseResult userInfo() {
        return userService.userInfo();
    }

    @PutMapping("/userInfo")
    @SystemLog(businessName = "更新用户信息")
    @ApiOperation("更新用户信息")
    public ResponseResult updateUserInfo(@RequestBody UserInfoDto userInfoDto) {
        return userService.updateUserInfo(userInfoDto);
    }

    @PostMapping("/register")
    @SystemLog(businessName = "用户注册")
    @ApiOperation("用户注册")
    public ResponseResult register(@RequestBody User user) {
        return userService.register(user);
    }
}
