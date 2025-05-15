import axios from 'axios'

// 创建 axios 实例
const api = axios.create({
  baseURL: 'http://localhost:8080/u', // 替换为您的后端 API 地址
  timeout: 5000,
  headers: {
    'Content-Type': 'application/json'
  }
})

// 登录接口
export const login = async (username, password) => {
  try {
    const response = await api.post('/auth/login', {
      username,
      password
    })
    return response.data
  } catch (error) {
    throw error.response?.data || error.message
  }
}

// 获取用户信息接口
export const getUserInfo = async () => {
  try {
    const response = await api.get('/auth/user-info')
    return response.data
  } catch (error) {
    throw error.response?.data || error.message
  }
} 