package com.xiaohai.controller;

import com.xiaohai.domain.ResponseResult;
import com.xiaohai.domain.dto.UserAddDto;
import com.xiaohai.domain.dto.UserQueryDto;
import com.xiaohai.domain.entity.User;
import com.xiaohai.domain.vo.PageVo;
import com.xiaohai.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/system/user")
public class UserController {
    @Autowired
    private UserService userService;
    @GetMapping("/list")
    public ResponseResult<PageVo> listPage(Integer pageNum, Integer pageSize, UserQueryDto userQueryDto) {
        //调用service
        return userService.listPage(pageNum,pageSize,userQueryDto);
    }

    @PostMapping
    public ResponseResult addUser(@RequestBody UserAddDto userDto) {
        return userService.addUser(userDto);
    }

    @GetMapping("/{id}")
    public ResponseResult getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @PutMapping
    public ResponseResult updateUser(@RequestBody UserAddDto userAddDto) {
        return userService.updateUser(userAddDto);
    }

    @DeleteMapping("/{id}")
    public ResponseResult deleteUserById(@PathVariable Long id) {
        return userService.deleteUserById(id);
    }
}
