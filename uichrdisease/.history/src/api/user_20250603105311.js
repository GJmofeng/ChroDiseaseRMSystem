import request from '@/utils/request'

// 获取用户列表
export function getUserList(params) {
  return request({
    url: '/user/list',
    method: 'get',
    params,
    headers: {
      'Content-Type': 'application/json',
      'Accept': 'application/json'
    }
  })
}

// 添加用户
export function addUser(data) {
  return request({
    url: '/user/add',
    method: 'post',
    data,
    headers: {
      'Content-Type': 'application/json',
      'Accept': 'application/json'
    }
  })
}

// 更新用户
export function updateUser(data) {
  return request({
    url: '/user/update',
    method: 'post',
    data,
    headers: {
      'Content-Type': 'application/json',
      'Accept': 'application/json'
    }
  })
}

// 批量删除用户
export function deleteBatchUser(ids) {
  return request({
    url: '/user/deleteBatch',
    method: 'post',
    data: ids,
    headers: {
      'Content-Type': 'application/json',
      'Accept': 'application/json'
    }
  })
}

// 重置密码
export function resetPassword(id) {
  return request({
    url: '/user/resetPwd',
    method: 'post',
    data: { id },
    headers: {
      'Content-Type': 'application/json',
      'Accept': 'application/json'
    }
  })
}

// 获取单个用户信息
export function getUserInfo(id) {
  return request({
    url: `/user/getOne/${id}`,
    method: 'get',
    headers: {
      'Content-Type': 'application/json',
      'Accept': 'application/json'
    }
  })
} 