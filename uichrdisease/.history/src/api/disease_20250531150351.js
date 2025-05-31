import request from '@/utils/request'

// 分页获取疾病列表
export const getDiseaseList = async (page = 1, pageSize = 10, diseaseName = '', diseaseCode = '') => {
  try {
    const response = await request({
      url: '/disease/list',
      method: 'get',
      params: { page, pageSize, diseaseName, diseaseCode }
    })
    return response
  } catch (error) {
    throw error
  }
}

// 添加疾病
export const addDisease = async (disease) => {
  try {
    const response = await request({
      url: '/disease/add',
      method: 'post',
      data: disease
    })
    return response
  } catch (error) {
    throw error
  }
}

// 更新疾病信息
export const updateDisease = async (disease) => {
  try {
    const response = await request({
      url: '/disease/edit',
      method: 'put',
      data: disease
    })
    return response
  } catch (error) {
    throw error
  }
}

// 删除疾病
export const deleteDisease = async (id) => {
  try {
    const response = await request({
      url: `/disease/delete/${id}`,
      method: 'delete'
    })
    return response
  } catch (error) {
    throw error
  }
}

// 获取疾病详情
export const getDiseaseDetail = async (id) => {
  try {
    const response = await request({
      url: `/disease/detail/${id}`,
      method: 'get'
    })
    return response
  } catch (error) {
    throw error
  }
}