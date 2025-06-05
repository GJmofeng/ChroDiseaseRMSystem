package com.gxwz.syschronicdisease.controller;

import com.gxwz.syschronicdisease.entity.Result;
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
    public Result getRoleList() {
        List<SysRole> roles = roleService.listAllRoles();
        return new Result("操作成功", roles, 200);
    }

    /**
     * 添加角色
     */
    @PostMapping("/role/add")
    public Result addRole(@RequestBody SysRole role) {
        boolean success = roleService.addRole(role);
        return success ? new Result("操作成功", null, 200) : new Result("添加角色失败", null, 500);
    }

    /**
     * 更新角色
     */
    @PostMapping("/role/update")
    public Result updateRole(@RequestBody SysRole role) {
        boolean success = roleService.updateRole(role);
        return success ? new Result("操作成功", null, 200) : new Result("更新角色失败", null, 500);
    }

    /**
     * 删除角色
     */
    @DeleteMapping("/role/delete/{id}")
    public Result deleteRole(@PathVariable Long id) {
        boolean success = roleService.deleteRole(id);
        return success ? new Result("操作成功", null, 200) : new Result("删除角色失败", null, 500);
    }

    /**
     * 获取菜单列表
     */
    @GetMapping("/menu/list")
    public Result getMenuList() {
        List<SysMenu> menus = menuService.getMenuTree();
        return new Result("操作成功", menus, 200);
    }

    /**
     * 获取角色的菜单权限ID列表
     */
    @GetMapping("/role/{roleId}/permissions")
    public Result getRolePermissions(@PathVariable Long roleId) {
        List<Long> menuIds = roleService.getRoleMenuIds(roleId);
        return new Result("操作成功", menuIds, 200);
    }

    /**
     * 设置角色的菜单权限
     */
    @PostMapping("/role/{roleId}/permissions")
    public Result setRolePermissions(@PathVariable Long roleId, @RequestBody List<Long> menuIds) {
        boolean success = roleService.assignRoleMenus(roleId, menuIds);
        return success ? new Result("操作成功", null, 200) : new Result("设置角色权限失败", null, 500);
    }

    /**
     * 获取用户的角色ID列表
     */
    @GetMapping("/user/{userId}/roles")
    public Result getUserRoles(@PathVariable Long userId) {
        List<Long> roleIds = userRoleService.getUserRoleIds(userId);
        return new Result("操作成功", roleIds, 200);
    }

    /**
     * 设置用户的角色
     */
    @PostMapping("/user/{userId}/roles")
    public Result setUserRoles(@PathVariable Long userId, @RequestBody List<Long> roleIds) {
        boolean success = userRoleService.assignUserRoles(userId, roleIds);
        return success ? new Result("操作成功", null, 200) : new Result("设置用户角色失败", null, 500);
    }
} 