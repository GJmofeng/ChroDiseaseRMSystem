package com.gxwz.syschronicdisease.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gxwz.syschronicdisease.entity.SysUserRole;
import com.gxwz.syschronicdisease.mapper.SysUserRoleMapper;
import com.gxwz.syschronicdisease.service.SysUserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户角色关联服务实现类
 */
@Service
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper, SysUserRole> implements SysUserRoleService {

    @Autowired
    private SysUserRoleMapper userRoleMapper;

    @Override
    public List<Long> getUserRoleIds(Long userId) {
        return userRoleMapper.selectRoleIdsByUserId(userId);
    }

    @Override
    @Transactional
    public boolean assignUserRoles(Long userId, List<Long> roleIds) {
        // 先删除原有的用户角色关联
        userRoleMapper.deleteByUserId(userId);
        
        // 如果没有需要分配的角色，直接返回成功
        if (roleIds == null || roleIds.isEmpty()) {
            return true;
        }
        
        // 重新添加新的用户角色关联
        List<SysUserRole> userRoleList = new ArrayList<>();
        for (Long roleId : roleIds) {
            SysUserRole userRole = new SysUserRole();
            userRole.setUserId(userId);
            userRole.setRoleId(roleId);
            userRoleList.add(userRole);
        }
        
        return saveBatch(userRoleList);
    }
} 