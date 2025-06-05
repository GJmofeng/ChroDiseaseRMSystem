package com.gxwz.syschronicdisease.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gxwz.syschronicdisease.entity.SysRole;

import java.util.List;

/**
 * 角色服务接口
 */
public interface SysRoleService extends IService<SysRole> {

    /**
     * 获取所有角色列表
     * @return 角色列表
     */
    List<SysRole> listAllRoles();

    /**
     * 添加角色
     * @param role 角色信息
     * @return 是否成功
     */
    boolean addRole(SysRole role);

    /**
     * 更新角色
     * @param role 角色信息
     * @return 是否成功
     */
    boolean updateRole(SysRole role);

    /**
     * 删除角色
     * @param id 角色ID
     * @return 是否成功
     */
    boolean deleteRole(Long id);

    /**
     * 获取角色的菜单权限ID列表
     * @param roleId 角色ID
     * @return 菜单权限ID列表
     */
    List<Long> getRoleMenuIds(Long roleId);

    /**
     * 设置角色的菜单权限
     * @param roleId 角色ID
     * @param menuIds 菜单权限ID列表
     * @return 是否成功
     */
    boolean assignRoleMenus(Long roleId, List<Long> menuIds);
} 