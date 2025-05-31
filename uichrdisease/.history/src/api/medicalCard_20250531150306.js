import request from '@/utils/request'

// 获取医疗卡列表
export const getAllMedicalCards = async (fullname = '', medicalName = '') => {
  try {
    const response = await request({
      url: '/medicalCard/getAll',
      method: 'get',
      params: { fullname, medicalName }
    })
    return response
  } catch (error) {
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
    throw error
  }
}

// 根据卡号查询医疗卡
export const getMedicalCardByCardId = async (cardId) => {
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