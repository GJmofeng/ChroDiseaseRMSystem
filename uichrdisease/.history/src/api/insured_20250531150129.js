import request from '@/utils/request'

// 分页获取参保人列表
export const getInsuredsByPage = async (pageNum = 1, pageSize = 10) => {
  try {
    const response = await request({
      url: '/insureds/page',
      method: 'get',
      params: { pageNum, pageSize }
    })
    return response
  } catch (error) {
    throw error
  }
}

// 获取所有参保人
export const getAllInsureds = async () => {
  try {
    const response = await request({
      url: '/insureds/getAll',
      method: 'get'
    })
    return response
  } catch (error) {
    throw error
  }
}

// 添加参保人
export const addInsured = async (insured) => {
  try {
    const response = await request({
      url: '/insureds/add',
      method: 'post',
      data: insured
    })
    return response
  } catch (error) {
    throw error
  }
}

// 更新参保人信息
export const updateInsured = async (insured) => {
  try {
    const response = await request({
      url: '/insureds/update',
      method: 'post',
      data: insured
    })
    return response
  } catch (error) {
    throw error
  }
}

// 删除参保人
export const deleteInsured = async (id) => {
  try {
    const response = await request({
      url: `/insureds/delete/${id}`,
      method: 'delete'
    })
    return response
  } catch (error) {
    throw error
  }
}

// 根据卡号查询参保人
export const getInsuredByCardId = async (cardId) => {
  try {
    const response = await request({
      url: `/insureds/getInsured/${cardId}`,
      method: 'get'
    })
    return response
  } catch (error) {
    throw error
  }
} 