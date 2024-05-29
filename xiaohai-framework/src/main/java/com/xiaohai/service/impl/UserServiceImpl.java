package com.xiaohai.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaohai.constants.SystemConstants;
import com.xiaohai.domain.ResponseResult;
import com.xiaohai.domain.dto.UserAddDto;
import com.xiaohai.domain.dto.UserDto;
import com.xiaohai.domain.dto.UserInfoDto;
import com.xiaohai.domain.dto.UserQueryDto;
import com.xiaohai.domain.entity.Role;
import com.xiaohai.domain.entity.User;
import com.xiaohai.domain.entity.UserRole;
import com.xiaohai.domain.vo.PageVo;
import com.xiaohai.domain.vo.UserInfoVo;
import com.xiaohai.domain.vo.UserVo;
import com.xiaohai.enums.AppHttpCodeEnum;
import com.xiaohai.exception.SystemException;
import com.xiaohai.mapper.UserMapper;
import com.xiaohai.mapper.UserRoleMapper;
import com.xiaohai.service.RoleService;
import com.xiaohai.service.UserRoleService;
import com.xiaohai.service.UserService;
import com.xiaohai.utils.BeanCopyUtils;
import com.xiaohai.utils.RegexUtils;
import com.xiaohai.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户表(User)表服务实现类
 *
 * @author makejava
 * @since 2024-05-09 11:31:53
 */
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private RoleService roleService;

    @Override
    public ResponseResult userInfo() {
        // 获取当前登录的用户id
        Long userId = SecurityUtils.getUserId();

        // 根据用户id查询用户信息
        User user = getById(userId);

        // 封装成vo对象
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(user, UserInfoVo.class);
        return ResponseResult.okResult(userInfoVo);
    }

    @Override
    public ResponseResult updateUserInfo(UserInfoDto userInfoDto) {
        // 更新用户信息
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getId, SecurityUtils.getUserId());

        User user = BeanCopyUtils.copyBean(userInfoDto, User.class);

        update(user, queryWrapper);
        //// 获取当前登录的用户id
        //Long userId = SecurityUtils.getUserId();
        //
        //// 根据用户id查询用户信息
        //User user = getById(userId);
        //
        //// 更新用户信息
        //user.setNickName(userInfoDto.getNickName());
        //user.setAvatar(userInfoDto.getAvatar());
        //user.setSex(userInfoDto.getSex());
        //
        //updateById(user);
        return ResponseResult.okResult();
    }

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    public ResponseResult register(User user) {
        //对数据进行非空判断
        if (!StringUtils.hasText(user.getUserName())) {
            throw new SystemException(AppHttpCodeEnum.USERNAME_NOT_NULL);
        }
        if (!StringUtils.hasText(user.getNickName())) {
            throw new SystemException(AppHttpCodeEnum.NICKNAME_NOT_NULL);
        }
        if (!StringUtils.hasText(user.getPassword())) {
            throw new SystemException(AppHttpCodeEnum.PASSWORD_NOT_NULL);
        }
        if (!StringUtils.hasText(user.getEmail())) {
            throw new SystemException(AppHttpCodeEnum.EMAIL_NOT_NULL);
        }
        //对数据进行是否存在的判断
        if(userNameExist(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
        }
        if(userNickNameExist(user.getNickName())) {
            throw new SystemException(AppHttpCodeEnum.NICKNAME_EXIST);
        }
        if(userEmailExist(user.getEmail())) {
            throw new SystemException(AppHttpCodeEnum.EMAIL_EXIST);
        }
        //对邮箱和密码进行正则判断
        if (RegexUtils.isEmailInvalid(user.getEmail())) {
            throw new SystemException(AppHttpCodeEnum.EMAIL_ERROR);
        }
        if (RegexUtils.isPasswordInvalid(user.getPassword())) {
            throw new SystemException(AppHttpCodeEnum.PASSWORD_ERROR);
        }
        //对密码进行加密
        String encodePassword = passwordEncoder.encode(user.getPassword());

        //对数据进行封装
        user.setPassword(encodePassword);
        //存入数据库
        save(user);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult<PageVo> listPage(Integer pageNum, Integer pageSize, UserQueryDto userQueryDto) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.like(StringUtils.hasText(userQueryDto.getUserName()), User::getUserName, userQueryDto.getUserName());
        queryWrapper.eq(StringUtils.hasText(userQueryDto.getPhonenumber()), User::getPhonenumber, userQueryDto.getPhonenumber());
        queryWrapper.eq(StringUtils.hasText(userQueryDto.getStatus()), User::getStatus, userQueryDto.getStatus());

        Page<User> page = new Page<>(pageNum, pageSize);
        Page<User> userPage = page(page, queryWrapper);
        List<UserVo> userVos = BeanCopyUtils.copyBeanList(userPage.getRecords(), UserVo.class);

        PageVo pageVo = new PageVo(userVos, userPage.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    @Transactional
    public ResponseResult addUser(UserAddDto userDto) {
        User user = BeanCopyUtils.copyBean(userDto, User.class);

        //对数据进行非空判断
        if (!StringUtils.hasText(user.getUserName())) {
            throw new SystemException(AppHttpCodeEnum.USERNAME_NOT_NULL);
        }
        if (!StringUtils.hasText(user.getNickName())) {
            throw new SystemException(AppHttpCodeEnum.NICKNAME_NOT_NULL);
        }
        if (!StringUtils.hasText(user.getPassword())) {
            throw new SystemException(AppHttpCodeEnum.PASSWORD_NOT_NULL);
        }
        //对数据进行是否存在的判断
        if(userNameExist(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
        }
        if(userNickNameExist(user.getNickName())) {
            throw new SystemException(AppHttpCodeEnum.NICKNAME_EXIST);
        }
        //如果填写了邮箱，对邮箱进行判断
        if (StringUtils.hasText(user.getEmail())) {
            //先判断邮箱是否存在
            if(userEmailExist(user.getEmail())) {
                throw new SystemException(AppHttpCodeEnum.EMAIL_EXIST);
            }
            //再对邮箱进行正则判断
            if (RegexUtils.isEmailInvalid(user.getEmail())) {
                throw new SystemException(AppHttpCodeEnum.EMAIL_ERROR);
            }
        }
        //对密码进行正则判断
        if (RegexUtils.isPasswordInvalid(user.getPassword())) {
            throw new SystemException(AppHttpCodeEnum.PASSWORD_ERROR);
        }
        //对密码进行加密
        String encodePassword = passwordEncoder.encode(user.getPassword());

        //对数据进行封装
        user.setPassword(encodePassword);
        //存入数据库
        save(user);
        List<UserRole> userRoles = userDto.getRoleIds()
                .stream()
                .map((roleId -> new UserRole(user.getId(), roleId)))
                .collect(Collectors.toList());
        userRoleService.saveBatch(userRoles);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getUserById(Long id) {
        //查询用户信息
        User user = getById(id);
        UserDto userDto = new UserDto();
        userDto.setUser(user);
        //根据用户id查询用户角色
        LambdaQueryWrapper<UserRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserRole::getUserId, id);
        List<UserRole> userRoles = userRoleService.list(queryWrapper);
        List<Long> roleIds = userRoles.stream()
                .map(userRole -> userRole.getRoleId())
                .collect(Collectors.toList());
        userDto.setRoleIds(roleIds);
        //查询角色
        List<Role> roles = roleService.list();
        userDto.setRoles(roles);
        return ResponseResult.okResult(userDto);
    }

    @Override
    @Transactional
    public ResponseResult updateUser(UserAddDto userDto) {
        User user = BeanCopyUtils.copyBean(userDto, User.class);

        ////对数据进行非空判断
        //if (!StringUtils.hasText(user.getUserName())) {
        //    throw new SystemException(AppHttpCodeEnum.USERNAME_NOT_NULL);
        //}
        //if (!StringUtils.hasText(user.getNickName())) {
        //    throw new SystemException(AppHttpCodeEnum.NICKNAME_NOT_NULL);
        //}
        //if (!StringUtils.hasText(user.getPassword())) {
        //    throw new SystemException(AppHttpCodeEnum.PASSWORD_NOT_NULL);
        //}
        ////对数据进行是否存在的判断
        //if(userNickNameExist(user.getNickName())) {
        //    throw new SystemException(AppHttpCodeEnum.NICKNAME_EXIST);
        //}
        ////如果填写了邮箱，对邮箱进行判断
        //if (StringUtils.hasText(user.getEmail())) {
        //    //先判断邮箱是否存在
        //    if(userEmailExist(user.getEmail())) {
        //        throw new SystemException(AppHttpCodeEnum.EMAIL_EXIST);
        //    }
        //    //再对邮箱进行正则判断
        //    if (RegexUtils.isEmailInvalid(user.getEmail())) {
        //        throw new SystemException(AppHttpCodeEnum.EMAIL_ERROR);
        //    }
        //}
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id",user.getId());
        updateWrapper.set("nick_name",user.getNickName());
        updateWrapper.set("phonenumber",user.getPhonenumber());
        updateWrapper.set("email",user.getEmail());
        updateWrapper.set("sex",user.getSex());
        updateWrapper.set("status",user.getStatus());
        update(updateWrapper);

        //删除旧的角色
        LambdaQueryWrapper<UserRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserRole::getUserId, user.getId());
        userRoleService.remove(queryWrapper);

        //保存新的角色
        List<UserRole> userRoles = userDto.getRoleIds().stream()
                                .map(roleId -> new UserRole(user.getId(), roleId))
                                .collect(Collectors.toList());

        userRoleService.saveBatch(userRoles);

        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteUserById(Long id) {
        //不能删除当前操作的用户
        if(id.equals(SecurityUtils.getUserId())) {
            throw new SystemException(AppHttpCodeEnum.DELETE_USER_ERROR);
        }
        //逻辑删除用户
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id",id);
        updateWrapper.set("del_flag", SystemConstants.IS_DELETED_TRUE);
        update(updateWrapper);

        return ResponseResult.okResult();
    }

    private boolean userEmailExist(String email) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.eq(User::getEmail, email);

        return count(queryWrapper) > 0;
    }

    private boolean userNickNameExist(String nickName) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.eq(User::getNickName, nickName);

        return count(queryWrapper) > 0;
    }

    private boolean userNameExist(String userName) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.eq(User::getUserName, userName);

        return count(queryWrapper) > 0;
    }
}

