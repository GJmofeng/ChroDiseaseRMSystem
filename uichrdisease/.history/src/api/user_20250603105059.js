import request from '@/utils/request'

// 用户注册
export function register(data) {
  return request({
    url: '/user/register',
    method: 'post',
    data
  })
}

// 用户登录
export function login(data) {
  return request({
    url: '/user/login',
    method: 'post',
    data
  })
}

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
    data,
    headers: {
      'Content-Type': 'application/json',
      'Accept': 'application/json'
    }
  })
}

// 更新用户信息
export function updateUser(data) {
  return request({
    url: '/user/update',
    method: 'post',
    data
  })
}

// 重置密码
export function resetPassword(data) {
  return request({
    url: '/user/resetPwd',
    method: 'post',
    data
  })
}

// 批量删除用户
export function deleteUsers(data) {
  return request({
    url: '/user/deleteBatch',
    method: 'post',
    data
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