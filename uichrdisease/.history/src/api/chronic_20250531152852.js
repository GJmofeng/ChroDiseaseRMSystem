import request from '@/utils/request'

// 获取慢性病证列表（分页）
export const getChronicCertList = async (params = {}) => {
  try {
    const response = await request({
      url: '/medicalCard/getAll',
      method: 'get',
      params: {
        page: params.page || 1,
        pageSize: params.pageSize || 10,
        fullname: params.insuredName?.trim() || '',
        medicalName: params.medicalName?.trim() || ''
      }
    })
    return response
  } catch (error) {
    throw error
  }
}

// 根据卡号查询
export const getMedicalByCardId = async (cardId) => {
  try {
    const response = await request({
      url: `/medicalCard/getMedical/${cardId}`,
      method: 'get'
    })
    return response
  } catch (error) {
    throw error
  }
}

// 新增慢性病证
export const addChronicCert = async (data) => {
  try {
    const response = await request({
      url: '/medicalCard/add',
      method: 'post',
      data
    })
    return response
  } catch (error) {
    throw error
  }
}

// 更新慢性病证
export const updateChronicCert = async (data) => {
  try {
    const response = await request({
      url: '/medicalCard/update',
      method: 'post',
      data
    })
    return response
  } catch (error) {
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
    throw error
  }
} 