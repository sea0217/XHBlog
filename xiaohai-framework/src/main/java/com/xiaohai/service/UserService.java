package com.xiaohai.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaohai.domain.ResponseResult;
import com.xiaohai.domain.dto.UserAddDto;
import com.xiaohai.domain.dto.UserDto;
import com.xiaohai.domain.dto.UserInfoDto;
import com.xiaohai.domain.dto.UserQueryDto;
import com.xiaohai.domain.entity.User;
import com.xiaohai.domain.vo.PageVo;

/**
 * 用户表(User)表服务接口
 *
 * @author makejava
 * @since 2024-05-09 11:31:53
 */
public interface UserService extends IService<User> {

    ResponseResult userInfo();

    ResponseResult updateUserInfo(UserInfoDto userInfoDto);

    ResponseResult register(User user);

    ResponseResult<PageVo> listPage(Integer pageNum, Integer pageSize, UserQueryDto userQueryDto);

    ResponseResult addUser(UserAddDto userDto);

    ResponseResult getUserById(Long id);

    ResponseResult updateUser(UserAddDto userDto);

    ResponseResult deleteUserById(Long id);
}

