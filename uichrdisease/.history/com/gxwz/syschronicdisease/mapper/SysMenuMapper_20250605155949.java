package com.gxwz.syschronicdisease.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gxwz.syschronicdisease.entity.SysMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 菜单Mapper接口
 */
@Mapper
public interface SysMenuMapper extends BaseMapper<SysMenu> {

    /**
     * 获取角色的菜单权限ID列表
     * @param roleId 角色ID
     * @return 菜单权限ID列表
     */
    @Select("SELECT m.id FROM sys_menu m " +
            "INNER JOIN sys_role_menu rm ON m.id = rm.menu_id " +
            "WHERE rm.role_id = #{roleId}")
    List<Long> selectMenuIdsByRoleId(Long roleId);
    
    /**
     * 获取用户的所有菜单
     * @param userId 用户ID
     * @return 菜单列表
     */
    @Select("SELECT DISTINCT m.* FROM sys_menu m " +
            "INNER JOIN sys_role_menu rm ON m.id = rm.menu_id " +
            "INNER JOIN sys_user_role ur ON rm.role_id = ur.role_id " +
            "WHERE ur.user_id = #{userId} " +
            "ORDER BY m.sort")
    List<SysMenu> selectMenusByUserId(Long userId);
} 