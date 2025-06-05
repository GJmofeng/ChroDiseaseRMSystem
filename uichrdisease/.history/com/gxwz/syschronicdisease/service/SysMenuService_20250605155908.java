package com.gxwz.syschronicdisease.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gxwz.syschronicdisease.entity.SysMenu;

import java.util.List;

/**
 * 菜单服务接口
 */
public interface SysMenuService extends IService<SysMenu> {

    /**
     * 获取菜单树形结构
     * @return 菜单树
     */
    List<SysMenu> getMenuTree();

    /**
     * 根据用户ID获取用户菜单列表
     * @param userId 用户ID
     * @return 菜单列表
     */
    List<SysMenu> getUserMenus(Long userId);

    /**
     * 根据角色ID获取角色拥有的菜单ID列表
     * @param roleId 角色ID
     * @return 菜单ID列表
     */
    List<Long> getRoleMenuIds(Long roleId);
} 