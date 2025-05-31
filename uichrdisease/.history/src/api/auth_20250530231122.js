import axios from 'axios'

// 创建 axios 实例
const api = axios.create({
  baseURL: '/user', // 使用相对路径，让 Vite 代理处理
  timeout: 5000,
  headers: {
    'Content-Type': 'application/json'
  },
  withCredentials: true // 允许携带 cookie
})

// 添加响应拦截器
api.interceptors.response.use(
  response => {
    // 直接返回响应数据
    return response.data
  },
  error => {
    // 处理错误响应
    if (error.response) {
      // 服务器返回错误状态码
      console.error('API Error:', error.response.data)
      return Promise.reject(error.response.data)
    } else if (error.request) {
      // 请求发送失败
      console.error('Network Error:', error.request)
      return Promise.reject({ msg: '网络请求失败，请检查网络连接' })
    } else {
      // 其他错误
      console.error('Request Error:', error.message)
      return Promise.reject({ msg: '请求配置错误' })
    }
  }
)

// 登录接口
export const login = async (userid, password) => {
  try {
    const response = await api.post('/login', {
      userid,
      password
    })
    return response
  } catch (error) {
    console.error('Login Error:', error)
    throw error
  }
}

// 获取用户信息接口
export const getUserInfo = async () => {
  try {
    const response = await api.get('/getOne')
    return response
  } catch (error) {
    console.error('GetUserInfo Error:', error)
    throw error
  }
} 