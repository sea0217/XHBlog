package com.xiaohai.controller;

import com.xiaohai.domain.ResponseResult;
import com.xiaohai.domain.entity.Menu;
import com.xiaohai.domain.vo.MenuUpdateVo;
import com.xiaohai.domain.vo.MenuVo;
import com.xiaohai.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/system/menu")
public class MenuController {
    @Autowired
    private MenuService menuService;
    @GetMapping("/treeselect")
    public ResponseResult<MenuVo> getMenuList(){
        return menuService.getMenuList();
    }

    @GetMapping("/roleMenuTreeselect/{id}")
    public ResponseResult<MenuUpdateVo> roleMenuTreeSelect(@PathVariable Long id){
        return menuService.roleMenuTreeSelect(id);
    }

    @GetMapping("/list")
    public ResponseResult<List<Menu>> menuList(String status, String menuName){
        return menuService.menuList(status,menuName);
    }

    @PostMapping
    public ResponseResult addMenu(@RequestBody Menu menu){
        return menuService.addMenu(menu);
    }

    @GetMapping("{id}")
    public ResponseResult<Menu> getMenuById(@PathVariable Long id){
        return menuService.getMenuById(id);
    }

    @PutMapping
    public ResponseResult updateMenu(@RequestBody Menu menu){
        return menuService.updateMenu(menu);
    }

    @DeleteMapping
    public ResponseResult deleteMenu(@RequestBody List<Long> ids){
        return menuService.deleteMenu(ids);
    }
}
