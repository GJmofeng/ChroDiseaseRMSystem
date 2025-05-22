import axios from 'axios'
import { ElMessage } from 'element-plus'
import router from '@/router'

// 创建axios实例
const service = axios.create({
  timeout: 5000, // 请求超时时间
  withCredentials: true // 允许携带cookie
})

// 请求拦截器
service.interceptors.request.use(
  config => {
    // 添加token
    const token = localStorage.getItem('token')
    if (token) {
      config.headers['Authorization'] = `Bearer ${token}`
    }
    
    // 添加其他必要的请求头
    config.headers['Content-Type'] = 'application/json'
    config.headers['X-Requested-With'] = 'XMLHttpRequest'
    
    return config
  },
  error => {
    console.log(error)
    return Promise.reject(error)
  }
)

// 响应拦截器
service.interceptors.response.use(
  response => {
    const res = response.data
    
    // 如果返回的状态码不是200，说明接口有问题，应该提示错误信息
    if (res.code !== 200) {
      ElMessage({
        message: res.msg || '系统错误',
        type: 'error',
        duration: 5 * 1000
      })

      // 如果是401，说明未登录或token过期，需要跳转到登录页
      if (res.code === 401 || response.status === 401) {
        localStorage.removeItem('token')
        router.push('/login')
        return Promise.reject(new Error('未登录或登录已过期，请重新登录'))
      }
      
      // 如果是403，说明没有权限
      if (res.code === 403 || response.status === 403) {
        ElMessage({
          message: '对不起，您没有访问权限',
          type: 'error',
          duration: 5 * 1000
        })
        return Promise.reject(new Error('没有权限'))
      }
      
      return Promise.reject(new Error(res.msg || '系统错误'))
    } else {
      return res
    }
  },
  error => {
    // 处理网络错误或后端返回的错误
    let message = '请求失败'
    if (error.response) {
      switch (error.response.status) {
        case 400:
          message = '请求参数错误'
          break
        case 401:
          message = '未登录或登录已过期，请重新登录'
          localStorage.removeItem('token')
          router.push('/login')
          break
        case 403:
          message = '对不起，您没有访问权限'
          break
        case 404:
          message = '请求的资源不存在'
          break
        case 408:
          message = '请求超时，请稍后重试'
          break
        case 500:
          message = '服务器内部错误，请联系管理员'
          break
        case 501:
          message = '服务未实现'
          break
        case 502:
          message = '网关错误'
          break
        case 503:
          message = '服务不可用，请稍后重试'
          break
        case 504:
          message = '网关超时，请稍后重试'
          break
        default:
          message = error.response.data?.msg || '发生未知错误'
      }
    } else if (error.request) {
      message = '服务器无响应，请检查网络连接'
    } else {
      message = error.message
    }
    
    ElMessage({
      message: message,
      type: 'error',
      duration: 5 * 1000
    })
    return Promise.reject(error)
  }
)

export default service 