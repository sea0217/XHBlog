package com.xiaohai.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiaohai.domain.entity.Role;

import java.util.List;

/**
 * 角色信息表(Role)表数据库访问层
 *
 * @author makejava
 * @since 2024-05-19 18:14:27
 */
public interface RoleMapper extends BaseMapper<Role> {

    List<String> selectRoleKeyByUserId(Long userId);
}

