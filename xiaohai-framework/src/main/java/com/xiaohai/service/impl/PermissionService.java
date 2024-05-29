package com.xiaohai.service.impl;

import com.xiaohai.utils.SecurityUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("ps")
public class PermissionService {
    /**
     * 判断用户是否有权限
     * @param permission 要判断的权限
     * @return
     */
    public boolean hasPermission(String permission){
        // 如果是超级管理员，则返回true
        if (SecurityUtils.isAdmin()) {
            return true;
        }
        // 否则  获取当前登录用户所具有的权限列表 然后判断是否存在permission
        List<String> permissions = SecurityUtils.getLoginUser().getPermissions();
        return permissions.contains(permission);
    }
}
