import request from '@/utils/request'

// 登录接口
export const login = async (userid, password) => {
  try {
    const response = await request.post('/user/login', {
      userid,
      password
    })
    return response.data
  } catch (error) {
    throw error
  }
}

// 获取用户信息接口
export const getUserInfo = async () => {
  try {
    const response = await request.get('/user/getOne')
    return response.data
  } catch (error) {
    throw error
  }
} 