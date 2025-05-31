import request from '@/utils/request'

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
    throw error
  }
}

// 获取用户信息接口
export const getUserInfo = async () => {
  try {
    const response = await request.get('/api/user/getOne')
    return response
  } catch (error) {
    throw error
  }
} 