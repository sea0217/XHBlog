package com.xiaohai.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaohai.constants.SystemConstants;
import com.xiaohai.domain.ResponseResult;
import com.xiaohai.domain.entity.Menu;
import com.xiaohai.domain.entity.RoleMenu;
import com.xiaohai.domain.vo.MenuUpdateVo;
import com.xiaohai.domain.vo.MenuVo;
import com.xiaohai.enums.AppHttpCodeEnum;
import com.xiaohai.exception.SystemException;
import com.xiaohai.mapper.MenuMapper;
import com.xiaohai.service.MenuService;
import com.xiaohai.service.RoleMenuService;
import com.xiaohai.utils.BeanCopyUtils;
import com.xiaohai.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 菜单权限表(Menu)表服务实现类
 *
 * @author makejava
 * @since 2024-05-19 18:07:28
 */
@Service("menuService")
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {
    @Autowired
    private RoleMenuService roleMenuService;
    @Override
    public List<String> selectPermByUserId(Long id) {
        //如果是管理员，返回所有权限
        if (SecurityUtils.isAdmin()) {
            LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.in(Menu::getMenuType, SystemConstants.MENU, SystemConstants.BUTTON);
            queryWrapper.eq(Menu::getStatus, SystemConstants.STATUS_NORMAL);
            List<Menu> menus = list(queryWrapper);
            List<String> perms = menus.stream()
                    .map(Menu::getPerms)
                    .collect(Collectors.toList());
            return perms;
        }
        //否则返回所具有的权限
        return baseMapper.selectPermsByUserId(id);
    }

    @Override
    public List<Menu> selectRouterMenuTreeByUserId(Long userId) {
        List<Menu> menus = null;
        //判断是否为管理员
        if (SecurityUtils.isAdmin()) {
            //如果是管理员，返回所有符合要求的menu
            menus = baseMapper.selectAllRouterMenu();
        }else {
            //否则返回所具有的menu
            menus = baseMapper.selectRouterMenuTreeByUserId(userId);
        }
        //构建tree
        List<Menu> menuTree = buildMenuTree(menus, 0L);
        return menuTree;
    }

    @Override
    public ResponseResult<MenuVo> getMenuList() {
        //查询所有菜单
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Menu::getStatus, SystemConstants.STATUS_NORMAL);
        queryWrapper.orderByAsc(Menu::getOrderNum);
        queryWrapper.orderByAsc(Menu::getParentId);
        List<Menu> menus = list(queryWrapper);
        //封装vo
        List<MenuVo> menuVos = BeanCopyUtils.copyBeanList(menus, MenuVo.class);
        //遍历menuTree,将menu中的menuName设置为menuVo中的label
        for (Menu menu : menus) {
            for (MenuVo menuVo : menuVos) {
                if (menu.getId().equals(menuVo.getId())) {
                    menuVo.setLabel(menu.getMenuName());
                }
            }
        }

        List<MenuVo> menuVoTree = buildMenuVoTree(menuVos, 0L);

        //优化版
        //Map<Long, Menu> menuMap = new HashMap<>();
        //for (Menu menu : menuTree) {
        //    menuMap.put(menu.getId(), menu);
        //}
        //
        //for (MenuVo menuVo : menuVos) {
        //    Menu menu = menuMap.get(menuVo.getId());
        //    if (menu != null) {
        //        menuVo.setLabel(menu.getMenuName());
        //    }
        //}

        return ResponseResult.okResult(menuVoTree);
    }

    @Override
    public ResponseResult<MenuUpdateVo> roleMenuTreeSelect(Long id) {
        //查询roleId对应的所有菜单
        LambdaQueryWrapper<RoleMenu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RoleMenu::getRoleId, id);
        List<RoleMenu> roleMenus = roleMenuService.list(queryWrapper);

        List<Long> checkedKeys = roleMenus.stream()
                .map(roleMenu -> roleMenu.getMenuId())
                .collect(Collectors.toList());

        //查询所有菜单
        LambdaQueryWrapper<Menu> menuQueryWrapper = new LambdaQueryWrapper<>();
        menuQueryWrapper.eq(Menu::getStatus, SystemConstants.STATUS_NORMAL);
        menuQueryWrapper.orderByAsc(Menu::getOrderNum);
        menuQueryWrapper.orderByAsc(Menu::getParentId);
        List<Menu> menus = list(menuQueryWrapper);

        List<MenuVo> menuVos = BeanCopyUtils.copyBeanList(menus, MenuVo.class);
        //遍历menuTree,将menu中的menuName设置为menuVo中的label
        for (Menu menu : menus) {
            for (MenuVo menuVo : menuVos) {
                if (menu.getId().equals(menuVo.getId())) {
                    menuVo.setLabel(menu.getMenuName());
                }
            }
        }

        List<MenuVo> menuVoTree = buildMenuVoTree(menuVos, 0L);
        MenuUpdateVo menuUpdateVo = new MenuUpdateVo(menuVoTree, checkedKeys);


        return ResponseResult.okResult(menuUpdateVo);
    }

    @Override
    public ResponseResult<List<Menu>> menuList(String status, String menuName) {
        //查询所有菜单
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasText(menuName), Menu::getMenuName, menuName);
        queryWrapper.eq(StringUtils.hasText(status), Menu::getStatus, status);
        queryWrapper.orderByAsc(Menu::getOrderNum);
        queryWrapper.orderByAsc(Menu::getParentId);
        List<Menu> menus = list(queryWrapper);

        return ResponseResult.okResult(menus);
    }

    @Override
    public ResponseResult addMenu(Menu menu) {
        save(menu);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult<Menu> getMenuById(Long id) {
        return ResponseResult.okResult(getById(id));
    }

    @Override
    public ResponseResult updateMenu(Menu menu) {
        //能够修改菜单，但是修改的时候不能把父菜单设置为当前菜单，如果设置了需要给出相应的提示。并且修改失败。
        if (menu.getParentId().equals(menu.getId())) {
            return ResponseResult.errorResult(AppHttpCodeEnum.CANNOT_SET_CHILD_AS_PARENT);
        }
        updateById(menu);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteMenu(List<Long> ids) {
        //删除菜单之前需要判断该菜单是否被角色引用，如果被引用需要给出相应的提示。
        for (Long id : ids) {
            LambdaQueryWrapper<RoleMenu> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(RoleMenu::getMenuId, id);
            int count = roleMenuService.count(queryWrapper);
            if (count > 0) {
                return ResponseResult.errorResult(AppHttpCodeEnum.HAVE_MENU_REFERENCED);
            }
        }
        //逻辑删除菜单
        LambdaUpdateWrapper<Menu> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.in(Menu::getId, ids);
        updateWrapper.set(Menu::getDelFlag, SystemConstants.IS_DELETED_TRUE);
        update(updateWrapper);

        return ResponseResult.okResult();
    }

    private List<Menu> buildMenuTree(List<Menu> menus, long parentId)  {
        List<Menu> menuTree = menus.stream()
                .filter(menu -> menu.getParentId().equals(parentId))
                .map(menu -> menu.setChildren(buildMenuTree(menus, menu.getId())))
                .collect(Collectors.toList());
        return menuTree;
    }

    private List<MenuVo> buildMenuVoTree(List<MenuVo> menuVos, long parentId)  {
        List<MenuVo> menuVoTree = menuVos.stream()
                .filter(menuVo -> menuVo.getParentId().equals(parentId))
                .map(menuVo -> menuVo.setChildren(buildMenuVoTree(menuVos, menuVo.getId())))
                .collect(Collectors.toList());
        return menuVoTree;
    }
}

