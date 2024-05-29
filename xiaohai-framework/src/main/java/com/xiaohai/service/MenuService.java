package com.xiaohai.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaohai.domain.ResponseResult;
import com.xiaohai.domain.entity.Menu;
import com.xiaohai.domain.vo.MenuUpdateVo;
import com.xiaohai.domain.vo.MenuVo;

import java.util.List;

/**
 * 菜单权限表(Menu)表服务接口
 *
 * @author makejava
 * @since 2024-05-19 18:07:28
 */
public interface MenuService extends IService<Menu> {

    List<String> selectPermByUserId(Long id);

    List<Menu> selectRouterMenuTreeByUserId(Long userId);

    ResponseResult<MenuVo> getMenuList();

    ResponseResult<MenuUpdateVo> roleMenuTreeSelect(Long id);

    ResponseResult<List<Menu>> menuList(String status, String menuName);

    ResponseResult addMenu(Menu menu);

    ResponseResult<Menu> getMenuById(Long id);

    ResponseResult updateMenu(Menu menu);

    ResponseResult deleteMenu(List<Long> ids);
}

