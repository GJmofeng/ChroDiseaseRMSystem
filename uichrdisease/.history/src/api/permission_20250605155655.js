import request from '@/utils/request'

// 获取菜单列表
export function getMenuList() {
  return request({
    url: '/permission/menu/list',
    method: 'get'
  })
}

// 获取角色列表
export function getRoleList() {
  return request({
    url: '/permission/role/list',
    method: 'get'
  })
}

// 添加角色
export function addRole(data) {
  return request({
    url: '/permission/role/add',
    method: 'post',
    data
  })
}

// 更新角色
export function updateRole(data) {
  return request({
    url: '/permission/role/update',
    method: 'post',
    data
  })
}

// 删除角色
export function deleteRole(id) {
  return request({
    url: `/permission/role/delete/${id}`,
    method: 'delete'
  })
}

// 获取角色权限
export function getRolePermission(roleId) {
  return request({
    url: `/permission/role/${roleId}/permissions`,
    method: 'get'
  })
}

// 设置角色权限
export function setRolePermission(roleId, menuIds) {
  return request({
    url: `/permission/role/${roleId}/permissions`,
    method: 'post',
    data: menuIds
  })
}

// 获取用户角色
export function getUserRoles(userId) {
  return request({
    url: `/permission/user/${userId}/roles`,
    method: 'get'
  })
}

// 设置用户角色
export function setUserRoles(userId, roleIds) {
  return request({
    url: `/permission/user/${userId}/roles`,
    method: 'post',
    data: roleIds
  })
} 