package com.gxwz.syschronicdisease.controller;

import com.gxwz.syschronicdisease.common.Result;
import com.gxwz.syschronicdisease.entity.SysMenu;
import com.gxwz.syschronicdisease.entity.SysRole;
import com.gxwz.syschronicdisease.service.SysMenuService;
import com.gxwz.syschronicdisease.service.SysRoleService;
import com.gxwz.syschronicdisease.service.SysUserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 权限管理控制器
 */
@RestController
@RequestMapping("/permission")
public class PermissionController {

    @Autowired
    private SysRoleService roleService;

    @Autowired
    private SysMenuService menuService;

    @Autowired
    private SysUserRoleService userRoleService;

    /**
     * 获取角色列表
     */
    @GetMapping("/role/list")
    public Result<List<SysRole>> getRoleList() {
        List<SysRole> roles = roleService.listAllRoles();
        return Result.success(roles);
    }

    /**
     * 添加角色
     */
    @PostMapping("/role/add")
    public Result<Void> addRole(@RequestBody SysRole role) {
        boolean success = roleService.addRole(role);
        return success ? Result.success() : Result.error("添加角色失败");
    }

    /**
     * 更新角色
     */
    @PostMapping("/role/update")
    public Result<Void> updateRole(@RequestBody SysRole role) {
        boolean success = roleService.updateRole(role);
        return success ? Result.success() : Result.error("更新角色失败");
    }

    /**
     * 删除角色
     */
    @DeleteMapping("/role/delete/{id}")
    public Result<Void> deleteRole(@PathVariable Long id) {
        boolean success = roleService.deleteRole(id);
        return success ? Result.success() : Result.error("删除角色失败");
    }

    /**
     * 获取菜单列表
     */
    @GetMapping("/menu/list")
    public Result<List<SysMenu>> getMenuList() {
        List<SysMenu> menus = menuService.getMenuTree();
        return Result.success(menus);
    }

    /**
     * 获取角色的菜单权限ID列表
     */
    @GetMapping("/role/{roleId}/permissions")
    public Result<List<Long>> getRolePermissions(@PathVariable Long roleId) {
        List<Long> menuIds = roleService.getRoleMenuIds(roleId);
        return Result.success(menuIds);
    }

    /**
     * 设置角色的菜单权限
     */
    @PostMapping("/role/{roleId}/permissions")
    public Result<Void> setRolePermissions(@PathVariable Long roleId, @RequestBody List<Long> menuIds) {
        boolean success = roleService.assignRoleMenus(roleId, menuIds);
        return success ? Result.success() : Result.error("设置角色权限失败");
    }

    /**
     * 获取用户的角色ID列表
     */
    @GetMapping("/user/{userId}/roles")
    public Result<List<Long>> getUserRoles(@PathVariable Long userId) {
        List<Long> roleIds = userRoleService.getUserRoleIds(userId);
        return Result.success(roleIds);
    }

    /**
     * 设置用户的角色
     */
    @PostMapping("/user/{userId}/roles")
    public Result<Void> setUserRoles(@PathVariable Long userId, @RequestBody List<Long> roleIds) {
        boolean success = userRoleService.assignUserRoles(userId, roleIds);
        return success ? Result.success() : Result.error("设置用户角色失败");
    }
} 