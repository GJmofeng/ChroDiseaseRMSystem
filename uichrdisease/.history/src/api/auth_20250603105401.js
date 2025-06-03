import request from '@/utils/request'

// 登录接口
export const login = async (userid, password) => {
  try {
    const response = await request({
      url: '/user/login',
      method: 'post',
      data: JSON.stringify({
        userid: userid,
        password: password
      }),
      headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json'
      }
    })
    return response
  } catch (error) {
    console.error('登录请求错误:', error)
    throw error
  }
}

// 获取用户信息接口
export const getUserInfo = async () => {
  try {
    const response = await request.get('/user/getOne')
    return response
  } catch (error) {
    throw error
  }
} 

