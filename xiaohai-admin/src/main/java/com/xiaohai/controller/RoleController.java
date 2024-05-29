package com.xiaohai.controller;

import com.xiaohai.domain.ResponseResult;
import com.xiaohai.domain.dto.RoleAddDto;
import com.xiaohai.domain.dto.RoleQueryDto;
import com.xiaohai.domain.entity.Role;
import com.xiaohai.domain.vo.PageVo;
import com.xiaohai.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/system/role")
public class RoleController {
    @Autowired
    private RoleService roleService;
    @GetMapping("/listAllRole")
    public ResponseResult<List<Role>> listAllRole(){
        //调用service查询数据
        return roleService.listAllRole();
    }

    @GetMapping("/list")
    public ResponseResult<PageVo> listPage(Integer pageNum, Integer pageSize, RoleQueryDto roleQueryDto){
        return roleService.selectRoleList(pageNum,pageSize,roleQueryDto);
    }

    @PutMapping("/changeStatus")
    public ResponseResult changeStatus(@RequestBody RoleQueryDto roleQueryDto){
        return roleService.changeStatus(roleQueryDto);
    }

    @PostMapping
    public ResponseResult addRole(@RequestBody RoleAddDto roleAddDto){
        return roleService.addRole(roleAddDto);
    }

    @GetMapping("/{id}")
    public ResponseResult<Role> getRoleById(@PathVariable Long id) {
        return roleService.getRoleById(id);
    }

    @PutMapping
    public ResponseResult updateRole(@RequestBody RoleAddDto roleAddDto){
        return roleService.updateRole(roleAddDto);
    }

    @DeleteMapping
    public ResponseResult deleteRole(@RequestBody List<Long> ids){
        return roleService.deleteRole(ids);
    }
}
