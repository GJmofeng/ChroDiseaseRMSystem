import request from '@/utils/request'
import { ElMessage } from 'element-plus'

// 获取慢性病证列表
export const getChronicCertList = async ({ page = 1, pageSize = 10, insuredName = '', medicalName = '' } = {}) => {
  try {
    const response = await request({
      url: '/medicalCard/getAll',
      method: 'get',
      params: { 
        page,
        pageSize,
        fullname: insuredName?.trim(),
        medicalName: medicalName?.trim()
      }
    })
    return response
  } catch (error) {
    ElMessage.error(error.response?.data?.message || '获取数据失败')
    throw error
  }
}

// 根据卡号查询医疗卡
export const getMedicalByCardId = async (cardId) => {
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

// 添加慢性病证
export const addChronicCert = async (chronicCert) => {
  try {
    const response = await request({
      url: '/medicalCard/add',
      method: 'post',
      data: chronicCert
    })
    return response
  } catch (error) {
    ElMessage.error(error.response?.data?.message || '添加失败')
    throw error
  }
}

// 更新慢性病证
export const updateChronicCert = async (chronicCert) => {
  try {
    const response = await request({
      url: '/medicalCard/update',
      method: 'post',
      data: chronicCert
    })
    return response
  } catch (error) {
    ElMessage.error(error.response?.data?.message || '更新失败')
    throw error
  }
}

// 删除慢性病证
export const deleteChronicCert = async (id) => {
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