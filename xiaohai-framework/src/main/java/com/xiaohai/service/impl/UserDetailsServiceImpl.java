package com.xiaohai.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xiaohai.constants.SystemConstants;
import com.xiaohai.domain.entity.LoginUser;
import com.xiaohai.domain.entity.User;
import com.xiaohai.mapper.MenuMapper;
import com.xiaohai.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private MenuMapper menuMapper;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 根据用户名查询用户信息
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserName, username);
        User user = userMapper.selectOne(queryWrapper);
        // 判断是否查到用户 如果没查到抛出异常
        if (Objects.isNull(user)) {
            throw new RuntimeException("用户不存在");
        }
        // 返回用户信息
        // TODO 查询权限信息封装
        // TODO 如果是后台用户才需要查询权限封装
        if (user.getType().equals(SystemConstants.ADMIN)) {
            List<String> list = menuMapper.selectPermsByUserId(user.getId());
            return new LoginUser(user, list);
        }
        return new LoginUser(user, null);
    }
}
