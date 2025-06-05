package com.gxwz.syschronicdisease.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gxwz.syschronicdisease.entity.SysMenu;
import com.gxwz.syschronicdisease.mapper.SysMenuMapper;
import com.gxwz.syschronicdisease.service.SysMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 菜单服务实现类
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {

    @Autowired
    private SysMenuMapper menuMapper;

    @Override
    public List<SysMenu> getMenuTree() {
        // 获取所有菜单
        List<SysMenu> allMenus = list(new LambdaQueryWrapper<SysMenu>()
                .orderByAsc(SysMenu::getSort));
        
        // 构建树形结构
        return buildMenuTree(allMenus);
    }

    @Override
    public List<SysMenu> getUserMenus(Long userId) {
        // 获取用户菜单
        List<SysMenu> userMenus = menuMapper.selectMenusByUserId(userId);
        
        // 构建树形结构
        return buildMenuTree(userMenus);
    }

    @Override
    public List<Long> getRoleMenuIds(Long roleId) {
        return menuMapper.selectMenuIdsByRoleId(roleId);
    }
    
    /**
     * 构建菜单树形结构
     * @param menuList 菜单列表
     * @return 树形菜单列表
     */
    private List<SysMenu> buildMenuTree(List<SysMenu> menuList) {
        // 将菜单列表转换为Map，方便查找
        Map<Long, SysMenu> menuMap = menuList.stream()
                .collect(Collectors.toMap(SysMenu::getId, menu -> menu));
        
        List<SysMenu> rootMenus = new ArrayList<>();
        
        // 遍历所有菜单，构建父子关系
        for (SysMenu menu : menuList) {
            // 如果是顶级菜单
            if (menu.getParentId() == null || menu.getParentId() == 0) {
                rootMenus.add(menu);
            } else {
                // 如果有父菜单，则添加到父菜单的子菜单列表
                SysMenu parentMenu = menuMap.get(menu.getParentId());
                if (parentMenu != null) {
                    if (parentMenu.getChildren() == null) {
                        parentMenu.setChildren(new ArrayList<>());
                    }
                    parentMenu.getChildren().add(menu);
                }
            }
        }
        
        return rootMenus;
    }
} 