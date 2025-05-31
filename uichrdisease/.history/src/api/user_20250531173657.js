import request from '@/utils/request'

// 获取用户列表
export function getUserList(params) {
  return request({
    url: '/user/list',
    method: 'get',
    params
  })
}

// 添加用户
export function addUser(data) {
  return request({
    url: '/user/add',
    method: 'post',
    data
  })
}

// 更新用户
export function updateUser(data) {
  return request({
    url: '/user/update',
    method: 'post',
    data
  })
}

// 批量删除用户
export function deleteBatchUser(ids) {
  return request({
    url: '/user/deleteBatch',
    method: 'post',
    data: ids
  })
}

// 重置密码
export function resetPassword(id) {
  return request({
    url: '/user/resetPwd',
    method: 'post',
    data: { id }
  })
}

// 获取单个用户信息
export function getUserInfo(id) {
  return request({
    url: `/user/getOne/${id}`,
    method: 'get'
  })
} 