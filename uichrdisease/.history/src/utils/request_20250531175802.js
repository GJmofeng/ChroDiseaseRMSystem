import axios from 'axios'
import { ElMessage } from 'element-plus'

// 创建axios实例
const request = axios.create({
  baseURL: 'http://154.12.36.159:8080', // 添加完整的baseURL
  timeout: 5000, // 请求超时时间
  headers: {
    'Content-Type': 'application/json',
    'X-Requested-With': 'XMLHttpRequest',
    'Accept': 'application/json'
  }
})

// 请求拦截器
request.interceptors.request.use(
  config => {
    // 从localStorage获取token
    const token = localStorage.getItem('token')
    if (token) {
      config.headers['Authorization'] = `Bearer ${token}`
    }
    
    // 打印请求信息，用于调试
    console.log('Request:', {
      url: config.url,
      method: config.method,
      data: config.data,
      headers: config.headers
    })
    
    // 处理OPTIONS请求
    if (config.method === 'options') {
      config.headers['Access-Control-Allow-Origin'] = '*'
      config.headers['Access-Control-Allow-Methods'] = 'GET,PUT,POST,DELETE,PATCH,OPTIONS'
      config.headers['Access-Control-Allow-Headers'] = 'Content-Type, Authorization'
    }
    
    return config
  },
  error => {
    console.error('请求错误:', error)
    return Promise.reject(error)
  }
)

// 响应拦截器
request.interceptors.response.use(
  response => {
    // 打印响应信息，用于调试
    console.log('Response:', {
      status: response.status,
      data: response.data,
      headers: response.headers
    })
    
    const res = response.data
    
    // 如果返回的状态码不是200，说明接口请求有误
    if (res.code !== 200) {
      ElMessage({
        message: res.msg || '请求失败',
        type: 'error',
        duration: 5 * 1000
      })
      
      // 401: 未登录或token过期
      if (res.code === 401) {
        // 清除本地token
        localStorage.removeItem('token')
        // 跳转到登录页
        window.location.href = '/login'
      }
      
      return Promise.reject(new Error(res.msg || '请求失败'))
    } else {
      return res
    }
  },
  error => {
    console.error('响应错误:', error)
    // 打印详细的错误信息
    if (error.response) {
      console.error('错误响应:', {
        status: error.response.status,
        data: error.response.data,
        headers: error.response.headers
      })
    }
    ElMessage({
      message: error.message || '请求失败',
      type: 'error',
      duration: 5 * 1000
    })
    return Promise.reject(error)
  }
)

export default request 