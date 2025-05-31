import request from '@/utils/request'

// 获取行政区划树形结构
export const getDivisionTree = async (name) => {
  try {
    const response = await request({
      url: '/division/tree',
      method: 'get',
      params: { name }
    })
    return response
  } catch (error) {
    throw error
  }
}

// 获取行政区划列表
export const getDivisionList = async (dname) => {
  try {
    const response = await request({
      url: '/division/getall',
      method: 'get',
      params: { dname }
    })
    return response
  } catch (error) {
    throw error
  }
}

// 获取单个行政区划
export const getDivision = async (id) => {
  try {
    const response = await request({
      url: `/division/getone/${id}`,
      method: 'get'
    })
    return response
  } catch (error) {
    throw error
  }
}

// 添加行政区划
export const addDivision = async (division) => {
  try {
    const response = await request({
      url: '/division/add',
      method: 'post',
      data: division
    })
    return response
  } catch (error) {
    throw error
  }
}

// 更新行政区划
export const updateDivision = async (division) => {
  try {
    const response = await request({
      url: '/division/update',
      method: 'post',
      data: division
    })
    return response
  } catch (error) {
    throw error
  }
}

// 删除行政区划
export const deleteDivision = async (id) => {
  try {
    const response = await request({
      url: `/division/delete/${id}`,
      method: 'delete'
    })
    return response
  } catch (error) {
    throw error
  }
}

// 获取子节点
export const getChildren = async (parent) => {
  try {
    const response = await request({
      url: '/division/children',
      method: 'get',
      params: { parent }
    })
    return response
  } catch (error) {
    throw error
  }
} 