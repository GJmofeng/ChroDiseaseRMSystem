import request from '@/utils/request'

// 获取所有菜单权限列表
export function getMenuList() {
  return request({
    url: '/menu/list',
    method: 'get',
    headers: {
      'Content-Type': 'application/json',
      'Accept': 'application/json'
    }
  })
}

// 获取指定用户的菜单权限
export function getUserMenus(userid) {
  return request({
    url: `/menu/getRole/${encodeURIComponent(userid)}`,
    method: 'get',
    headers: {
      'Content-Type': 'application/json',
      'Accept': 'application/json'
    }
  })
}

// 添加或更新用户菜单权限
export function addUserMenus(menus) {
  return request({
    url: '/menu/add',
    method: 'post',
    data: menus,
    headers: {
      'Content-Type': 'application/json',
      'Accept': 'application/json'
    }
  })
}

// 更新单个菜单权限
export function updateMenu(menu) {
  return request({
    url: '/menu/update',
    method: 'post',
    data: menu,
    headers: {
      'Content-Type': 'application/json',
      'Accept': 'application/json'
    }
  })
}

// 删除菜单权限
export function deleteMenu(id) {
  return request({
    url: `/menu/delete/${id}`,
    method: 'post',
    headers: {
      'Content-Type': 'application/json',
      'Accept': 'application/json'
    }
  })
} 