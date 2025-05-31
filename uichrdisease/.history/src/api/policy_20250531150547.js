import request from '@/utils/request'

// 获取政策列表
export const getPolicyList = async () => {
  try {
    const response = await request({
      url: '/policy/list',
      method: 'get'
    })
    return response
  } catch (error) {
    throw error
  }
}

// 添加政策
export const addPolicy = async (policy) => {
  try {
    const response = await request({
      url: '/policy/add',
      method: 'post',
      data: policy
    })
    return response
  } catch (error) {
    throw error
  }
}

// 更新政策
export const updatePolicy = async (policy) => {
  try {
    const response = await request({
      url: '/policy/update',
      method: 'put',
      data: policy
    })
    return response
  } catch (error) {
    throw error
  }
}

// 删除政策
export const deletePolicy = async (id) => {
  try {
    const response = await request({
      url: `/policy/delete/${id}`,
      method: 'delete'
    })
    return response
  } catch (error) {
    throw error
  }
}

// 根据年度查询政策
export const getPolicyByYear = async (year) => {
  try {
    const response = await request({
      url: `/policy/getyear/${year}`,
      method: 'get'
    })
    return response
  } catch (error) {
    throw error
  }
} 