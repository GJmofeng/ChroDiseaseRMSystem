import request from '@/utils/request'
import { ElMessage } from 'element-plus'

// 登录接口
export const login = async (userid, password) => {
  try {
    const response = await request({
      url: '/api/user/login',
      method: 'post',
      data: {
        userid,
        password
      }
    })
    return response
  } catch (error) {
    ElMessage.error(error.response?.data?.message || '登录失败')
    throw error
  }
}

// 获取用户信息接口
export const getUserInfo = async () => {
  try {
    const response = await request({
      url: '/api/user/getOne',
      method: 'get'
    })
    return response
  } catch (error) {
    ElMessage.error(error.response?.data?.message || '获取用户信息失败')
    throw error
  }
} 