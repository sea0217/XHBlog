package com.xiaohai.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaohai.domain.ResponseResult;
import com.xiaohai.domain.dto.RoleAddDto;
import com.xiaohai.domain.dto.RoleQueryDto;
import com.xiaohai.domain.entity.Role;
import com.xiaohai.domain.vo.PageVo;

import java.util.List;

/**
 * 角色信息表(Role)表服务接口
 *
 * @author makejava
 * @since 2024-05-19 18:14:28
 */
public interface RoleService extends IService<Role> {

    List<String> selectRoleKeyByUserId(Long id);

    ResponseResult<List<Role>> listAllRole();

    ResponseResult<PageVo> selectRoleList(Integer pageNum, Integer pageSize, RoleQueryDto roleQueryDto);

    ResponseResult changeStatus(RoleQueryDto roleQueryDto);

    ResponseResult addRole(RoleAddDto roleAddDto);

    ResponseResult<Role> getRoleById(Long id);

    ResponseResult updateRole(RoleAddDto roleAddDto);

    ResponseResult deleteRole(List<Long> ids);
}

