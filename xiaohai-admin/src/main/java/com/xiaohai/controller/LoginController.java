package com.xiaohai.controller;

import com.xiaohai.annotation.SystemLog;
import com.xiaohai.domain.ResponseResult;
import com.xiaohai.domain.entity.Menu;
import com.xiaohai.domain.entity.User;
import com.xiaohai.domain.vo.AdminUserInfoVo;
import com.xiaohai.domain.vo.RoutersVo;
import com.xiaohai.domain.vo.UserInfoVo;
import com.xiaohai.enums.AppHttpCodeEnum;
import com.xiaohai.exception.SystemException;
import com.xiaohai.service.LoginService;
import com.xiaohai.service.MenuService;
import com.xiaohai.service.RoleService;
import com.xiaohai.utils.BeanCopyUtils;
import com.xiaohai.utils.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Api(tags = "登录模块")
public class LoginController {
    @Autowired
    private LoginService loginService;
    @Autowired
    private MenuService menuService;
    @Autowired
    private RoleService roleService;
    @PostMapping("/user/login")
    @SystemLog(businessName = "用户登录")
    @ApiOperation(value = "用户登录", notes = "用户登录")
    public ResponseResult login(@RequestBody User user) {
        if (!StringUtils.hasText(user.getUserName())) {
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
        return loginService.login(user);
    }

    @GetMapping("/getInfo")
    public ResponseResult<AdminUserInfoVo> getInfo() {
        //获取当前登录的用户
        User user = SecurityUtils.getLoginUser().getUser();
        //根据用户id查询权限信息
        List<String> perms = menuService.selectPermByUserId(user.getId());
        //根据用户id查询角色信息
        List<String> roleKeyList = roleService.selectRoleKeyByUserId(user.getId());

        //封装数据
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(user, UserInfoVo.class);
        AdminUserInfoVo adminUserInfoVo = new AdminUserInfoVo(perms, roleKeyList, userInfoVo);

        return ResponseResult.okResult(adminUserInfoVo);
    }

    @GetMapping("/getRouters")
    public ResponseResult<RoutersVo> getRouters() {
        Long userId = SecurityUtils.getUserId();
        //查询menu结果是tree的形式
        List<Menu> menus = menuService.selectRouterMenuTreeByUserId(userId);
        //封装数据返回
        return ResponseResult.okResult(new RoutersVo(menus));
    }

    @PostMapping("/user/logout")
    @SystemLog(businessName = "用户退出登录")
    @ApiOperation(value = "用户退出登录", notes = "用户退出登录")
    public ResponseResult logout() {
        return loginService.logout();
    }
}
