package com.xiaohai.service.impl;

import com.xiaohai.constants.CacheConstants;
import com.xiaohai.domain.ResponseResult;
import com.xiaohai.domain.entity.LoginUser;
import com.xiaohai.domain.entity.User;

import com.xiaohai.service.LoginService;

import com.xiaohai.utils.JwtUtil;
import com.xiaohai.utils.RedisCache;
import com.xiaohai.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class SystemLoginServiceImpl implements LoginService {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private RedisCache redisCache;
    @Override
    public ResponseResult login(User user) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUserName(),user.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        // 判断是否认证通过
        if (Objects.isNull(authenticate)){
            throw new RuntimeException("用户名或密码错误");
        }
        // 获取userId，生成token
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        String userId = loginUser.getUser().getId().toString();
        String jwt = JwtUtil.createJWT(userId);
        // 把用户信息存入redis
        redisCache.setCacheObject(CacheConstants.ADMIN_LOGIN +userId,loginUser);

        // 把token和userinfo封装并返回
        // 用token封装返回
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("token",jwt);
        return ResponseResult.okResult(tokenMap);
    }

    @Override
    public ResponseResult logout() {
        // 获取用户id
        Long userId = SecurityUtils.getUserId();
        redisCache.deleteObject(CacheConstants.ADMIN_LOGIN + userId);
        return ResponseResult.okResult();
    }

    //@Override
    //public ResponseResult logout() {
    //    // 获取用户id
    //    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    //    LoginUser loginUser = (LoginUser) authentication.getPrincipal();
    //    Long userId = loginUser.getUser().getId();
    //    //删除redis中的用户信息
    //    redisCache.deleteObject(CacheConstants.BLOG_LOGIN+userId);
    //    return ResponseResult.okResult();
    //}
}
