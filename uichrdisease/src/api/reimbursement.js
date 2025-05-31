import request from '@/utils/request'

// 获取所有报销记录
export const getAllReimbursements = async () => {
  try {
    const response = await request({
      url: '/reimbursement/getAll',
      method: 'get'
    })
    return response
  } catch (error) {
    throw error
  }
}

// 添加报销记录
export const addReimbursement = async (reimbursement) => {
  try {
    const response = await request({
      url: '/reimbursement/add',
      method: 'post',
      data: reimbursement
    })
    return response
  } catch (error) {
    throw error
  }
}

// 更新报销记录
export const updateReimbursement = async (reimbursement) => {
  try {
    const response = await request({
      url: '/reimbursement/update',
      method: 'post',
      data: reimbursement
    })
    return response
  } catch (error) {
    throw error
  }
}

// 获取待审核的报销记录
export const getAuditReimbursements = async () => {
  try {
    const response = await request({
      url: '/reimbursement/getAudit',
      method: 'get'
    })
    return response
  } catch (error) {
    throw error
  }
}

// 获取待汇款的报销记录
export const getRemitReimbursements = async () => {
  try {
    const response = await request({
      url: '/reimbursement/getRemit',
      method: 'get'
    })
    return response
  } catch (error) {
    throw error
  }
}

// 获取报销统计信息
export const getReimbursementStatistics = async () => {
  try {
    const response = await request({
      url: '/reimbursement/getStatics',
      method: 'get'
    })
    return response
  } catch (error) {
    throw error
  }
} 