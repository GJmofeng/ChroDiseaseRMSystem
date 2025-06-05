package com.gxwz.syschronicdisease.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gxwz.syschronicdisease.entity.SysRoleMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Delete;

/**
 * 角色菜单关联Mapper接口
 */
@Mapper
public interface SysRoleMenuMapper extends BaseMapper<SysRoleMenu> {

    /**
     * 根据角色ID删除角色菜单关联关系
     * @param roleId 角色ID
     * @return 影响行数
     */
    @Delete("DELETE FROM sys_role_menu WHERE role_id = #{roleId}")
    int deleteByRoleId(Long roleId);
}