package com.xiaohai.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaohai.constants.SystemConstants;
import com.xiaohai.domain.ResponseResult;
import com.xiaohai.domain.dto.RoleAddDto;
import com.xiaohai.domain.dto.RoleQueryDto;
import com.xiaohai.domain.entity.Role;
import com.xiaohai.domain.entity.RoleMenu;
import com.xiaohai.domain.vo.PageVo;
import com.xiaohai.mapper.RoleMapper;
import com.xiaohai.service.RoleMenuService;
import com.xiaohai.service.RoleService;
import com.xiaohai.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 角色信息表(Role)表服务实现类
 *
 * @author makejava
 * @since 2024-05-19 18:14:28
 */
@Service("roleService")
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {
    @Autowired
    private RoleMenuService roleMenuService;

    @Override
    public List<String> selectRoleKeyByUserId(Long id) {
        //判断是否是管理员 如果是，返回集合中只需要有admin
        if (id == 1L) {
            List<String> roleKeys = new ArrayList<>();
            roleKeys.add("admin");
            return roleKeys;
        }
        //否则查询用户中所具有的角色信息
        return baseMapper.selectRoleKeyByUserId(id);
    }

    @Override
    public ResponseResult<List<Role>> listAllRole() {
        //查询所有角色
        List<Role> roles = baseMapper.selectList(null);
        return ResponseResult.okResult(roles);
    }

    @Override
    public ResponseResult<PageVo> selectRoleList(Integer pageNum, Integer pageSize, RoleQueryDto roleQueryDto) {
        //分页查询
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasText(roleQueryDto.getRoleName()), Role::getRoleName, roleQueryDto.getRoleName());
        queryWrapper.eq(StringUtils.hasText(roleQueryDto.getStatus()), Role::getStatus, roleQueryDto.getStatus());
        queryWrapper.orderByAsc(Role::getRoleSort);

        Page<Role> page = new Page<>(pageNum, pageSize);
        page(page, queryWrapper);

        return ResponseResult.okResult(new PageVo(page.getRecords(), page.getTotal()));
    }

    @Override
    public ResponseResult changeStatus(RoleQueryDto roleQueryDto) {
        //修改状态
        Long id = roleQueryDto.getRoleId();
        Role role = baseMapper.selectById(id);
        role.setStatus(roleQueryDto.getStatus());
        baseMapper.updateById(role);
        return ResponseResult.okResult();
    }

    @Override
    @Transactional
    public ResponseResult addRole(RoleAddDto roleAddDto) {
        //添加角色
        Role role = BeanCopyUtils.copyBean(roleAddDto, Role.class);
        save(role);
        //添加角色和菜单关联关系
        List<Long> menuIds = roleAddDto.getMenuIds();

        List<RoleMenu> roleMenus = new ArrayList<>();
        for (Long menuId : menuIds) {
            RoleMenu roleMenu = new RoleMenu(role.getId(), menuId);
            roleMenus.add(roleMenu);
        }

        roleMenuService.saveBatch(roleMenus);

        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult<Role> getRoleById(Long id) {
        //根据id查询
        Role role = baseMapper.selectById(id);

        return ResponseResult.okResult(role);
    }

    @Override
    @Transactional
    public ResponseResult updateRole(RoleAddDto roleAddDto) {
        //修改角色
        Role role = BeanCopyUtils.copyBean(roleAddDto, Role.class);
        updateById(role);
        //修改角色和菜单关联关系
        List<Long> menuIds = roleAddDto.getMenuIds();

        List<RoleMenu> roleMenus = new ArrayList<>();
        for (Long menuId : menuIds) {
            RoleMenu roleMenu = new RoleMenu(role.getId(), menuId);
            roleMenus.add(roleMenu);
        }

        LambdaUpdateWrapper<RoleMenu> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(RoleMenu::getRoleId, role.getId());
        roleMenuService.remove(updateWrapper);
        roleMenuService.saveBatch(roleMenus);

        return ResponseResult.okResult();
    }

    @Override
    @Transactional
    public ResponseResult deleteRole(List<Long> ids) {
        //逻辑删除角色
        LambdaUpdateWrapper<Role> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.in(Role::getId, ids);
        updateWrapper.set(Role::getDelFlag, SystemConstants.IS_DELETED_TRUE);
        update(updateWrapper);

        //删除角色和菜单关联关系
        LambdaUpdateWrapper<RoleMenu> roleMenuUpdateWrapper = new LambdaUpdateWrapper<>();
        roleMenuUpdateWrapper.in(RoleMenu::getRoleId, ids);
        roleMenuService.remove(roleMenuUpdateWrapper);

        return ResponseResult.okResult();
    }
}

