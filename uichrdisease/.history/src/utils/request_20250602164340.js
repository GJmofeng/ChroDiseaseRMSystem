import axios from 'axios'
import { ElMessage } from 'element-plus'
import router from '@/router'

// 创建axios实例
const request = axios.create({
  // baseURL: 'http://154.12.36.159:8080',
  // baseURL: 'http://localhost:8080',
  timeout: 15000,
  headers: {
    'Content-Type': 'application/json',
    'X-Requested-With': 'XMLHttpRequest',
    'Accept': 'application/json',
    'request-ajax': 'true'
  },
  withCredentials: true // 允许跨域携带cookie
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
    
    // 处理Result统一响应格式
    if (res && typeof res === 'object') {
      // 如果返回的是Result格式
      if (res.code !== undefined && res.data !== undefined) {
        // 如果code不是200，说明有错误
        if (res.code !== 200) {
          ElMessage({
            message: res.message || '请求失败',
            type: 'error',
            duration: 5 * 1000
          })
          
          // 401: 未登录或token过期
          if (res.code === 401) {
            localStorage.removeItem('token')
            router.push('/login')
          }
          
          return Promise.reject(new Error(res.message || '请求失败'))
        }
        
        // 正常返回数据
        return {
          code: res.code,
          data: res.data,
          message: res.message
        }
      }
      
      // 如果返回的是Map格式（包含data和total）
      if (res.data !== undefined && res.total !== undefined) {
        return {
          code: 200,
          data: res.data,
          total: res.total,
          message: 'success'
        }
      }
      
      // 如果不是Result格式，包装成Result格式
      return {
        code: 200,
        data: res,
        message: 'success'
      }
    }
    
    // 如果返回的不是对象，包装成Result格式
    return {
      code: 200,
      data: res,
      message: 'success'
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
    
    let errorMessage = '请求失败'
    if (error.response) {
      switch (error.response.status) {
        case 400:
          errorMessage = '请求错误'
          break
        case 401:
          errorMessage = '未授权，请重新登录'
          localStorage.removeItem('token')
          router.push('/login')
          break
        case 403:
          errorMessage = '拒绝访问'
          break
        case 404:
          errorMessage = '请求地址出错'
          break
        case 500:
          errorMessage = '服务器内部错误'
          break
        default:
          errorMessage = `请求失败(${error.response.status})`
      }
    } else if (error.request) {
      errorMessage = '服务器未响应'
    }
    
    ElMessage({
      message: errorMessage,
      type: 'error',
      duration: 5 * 1000
    })
    return Promise.reject(error)
  }
)

export default request