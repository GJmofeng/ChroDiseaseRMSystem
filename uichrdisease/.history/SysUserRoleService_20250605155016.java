package com.gxwz.syschronicdisease.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gxwz.syschronicdisease.entity.SysUserRole;

import java.util.List;

/**
 * 用户角色关联服务接口
 */
public interface SysUserRoleService extends IService<SysUserRole> {

    /**
     * 获取用户的角色ID列表
     * @param userId 用户ID
     * @return 角色ID列表
     */
    List<Long> getUserRoleIds(Long userId);

    /**
     * 设置用户角色
     * @param userId 用户ID
     * @param roleIds 角色ID列表
     * @return 是否成功
     */
    boolean assignUserRoles(Long userId, List<Long> roleIds);
}