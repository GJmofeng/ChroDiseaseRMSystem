package com.gxwz.syschronicdisease.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gxwz.syschronicdisease.entity.SysRole;
import com.gxwz.syschronicdisease.entity.SysRoleMenu;
import com.gxwz.syschronicdisease.mapper.SysRoleMapper;
import com.gxwz.syschronicdisease.mapper.SysMenuMapper;
import com.gxwz.syschronicdisease.mapper.SysRoleMenuMapper;
import com.gxwz.syschronicdisease.service.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 角色服务实现类
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

    @Autowired
    private SysRoleMapper roleMapper;

    @Autowired
    private SysMenuMapper menuMapper;

    @Autowired
    private SysRoleMenuMapper roleMenuMapper;

    @Override
    public List<SysRole> listAllRoles() {
        return list();
    }

    @Override
    public boolean addRole(SysRole role) {
        return save(role);
    }

    @Override
    public boolean updateRole(SysRole role) {
        return updateById(role);
    }

    @Override
    @Transactional
    public boolean deleteRole(Long id) {
        // 先删除角色菜单关联
        roleMenuMapper.deleteByRoleId(id);
        // 再删除角色
        return removeById(id);
    }

    @Override
    public List<Long> getRoleMenuIds(Long roleId) {
        return menuMapper.selectMenuIdsByRoleId(roleId);
    }

    @Override
    @Transactional
    public boolean assignRoleMenus(Long roleId, List<Long> menuIds) {
        // 先删除原有的角色菜单关联
        roleMenuMapper.deleteByRoleId(roleId);
        
        // 如果没有需要分配的菜单权限，直接返回成功
        if (menuIds == null || menuIds.isEmpty()) {
            return true;
        }
        
        // 重新添加新的角色菜单关联
        List<SysRoleMenu> roleMenuList = new ArrayList<>();
        for (Long menuId : menuIds) {
            SysRoleMenu roleMenu = new SysRoleMenu();
            roleMenu.setRoleId(roleId);
            roleMenu.setMenuId(menuId);
            roleMenuList.add(roleMenu);
        }
        
        return saveBatch(roleMenuList);
    }
} 