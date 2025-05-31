import request from '@/utils/request'
import { ElMessage } from 'element-plus'

// 获取医疗卡列表
export const getAllMedicalCards = async ({ page = 1, pageSize = 10, fullname = '', medicalName = '' } = {}) => {
  try {
    const response = await request({
      url: '/medicalCard/getAll',
      method: 'get',
      params: { 
        page,
        pageSize,
        fullname: fullname?.trim(),
        medicalName: medicalName?.trim()
      }
    })
    return response
  } catch (error) {
    ElMessage.error(error.response?.data?.message || '获取数据失败')
    throw error
  }
}

// 添加医疗卡
export const addMedicalCard = async (medicalCard) => {
  try {
    const response = await request({
      url: '/medicalCard/add',
      method: 'post',
      data: medicalCard
    })
    return response
  } catch (error) {
    ElMessage.error(error.response?.data?.message || '添加失败')
    throw error
  }
}

// 更新医疗卡信息
export const updateMedicalCard = async (medicalCard) => {
  try {
    const response = await request({
      url: '/medicalCard/update',
      method: 'post',
      data: medicalCard
    })
    return response
  } catch (error) {
    ElMessage.error(error.response?.data?.message || '更新失败')
    throw error
  }
}

// 删除医疗卡
export const deleteMedicalCard = async (id) => {
  try {
    const response = await request({
      url: `/medicalCard/delete/${id}`,
      method: 'delete'
    })
    return response
  } catch (error) {
    ElMessage.error(error.response?.data?.message || '删除失败')
    throw error
  }
}

// 根据卡号查询医疗卡
export const getMedicalCardByCardId = async (cardId) => {
  try {
    if (!cardId?.trim()) {
      return { code: 400, msg: '卡号不能为空' }
    }
    const response = await request({
      url: `/medicalCard/getMedical/${cardId.trim()}`,
      method: 'get'
    })
    return response
  } catch (error) {
    ElMessage.error(error.response?.data?.message || '查询失败')
    throw error
  }
}

// 获取政策列表
export const getPolicyList = async () => {
  try {
    const response = await request({
      url: '/policy/list',
      method: 'get'
    })
    return response
  } catch (error) {
    ElMessage.error(error.response?.data?.message || '获取政策列表失败')
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
    ElMessage.error(error.response?.data?.message || '添加失败')
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
    ElMessage.error(error.response?.data?.message || '更新失败')
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
    ElMessage.error(error.response?.data?.message || '删除失败')
    throw error
  }
}

// 根据年度查询政策
export const getPolicyByYear = async (year) => {
  try {
    if (!year?.trim()) {
      return { code: 400, msg: '年度不能为空' }
    }
    const response = await request({
      url: `/policy/getyear/${year.trim()}`,
      method: 'get'
    })
    return response
  } catch (error) {
    ElMessage.error(error.response?.data?.message || '查询失败')
    throw error
  }
} 