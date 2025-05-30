import axios from 'axios'

// 创建 axios 实例
const api = axios.create({
  // baseURL: 'http://localhost:8080/user', // 替换为实际的后端 API 地址
  
  timeout: 5000,
  headers: {
    'Content-Type': 'application/json'
  }
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
      return Promise.reject(error.response.data)
    } else if (error.request) {
      // 请求发送失败
      return Promise.reject({ msg: '网络请求失败，请检查网络连接' })
    } else {
      // 其他错误
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
    throw error
  }
}

// 获取用户信息接口
export const getUserInfo = async () => {
  try {
    const response = await api.get('/getOne')
    return response
  } catch (error) {
    throw error
  }
} 