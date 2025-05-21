import request from '@/utils/request'
import { ElMessage } from 'element-plus'

// 查询参合人员列表
export async function searchParticipationInfo(retryCount = 3) {
  try {
    const res = await request({
      url: '/insureds/getAll',
      method: 'get'
    })
    return res
  } catch (error) {
    if (retryCount > 0 && error.response?.status === 500) {
      ElMessage.warning('服务器响应超时，正在重试...')
      await new Promise(resolve => setTimeout(resolve, 1000))
      return searchParticipationInfo(retryCount - 1)
    }
    
    const errorMsg = error.response?.status === 500 
      ? '服务器内部错误，请确保后端服务正常运行'
      : error.message || '获取数据失败'
    
    ElMessage.error(errorMsg)
    throw error
  }
}

// 根据参合证号查询参合人员
export async function getInsuredByCardId(cardId) {
  try {
    if (!cardId) {
      ElMessage.warning('请输入参合证号')
      return { code: 400, msg: '参合证号不能为空' }
    }
    
    // 确保cardId是字符串类型
    const formattedCardId = String(cardId).trim()
    
    const res = await request({
      url: `/insureds/getMedical/${formattedCardId}`,
      url: `/insureds/getInsured/${formattedCardId}`,
      method: 'get'
    })
    return res
  } catch (error) {
    if (error.response?.status === 400) {
      ElMessage.warning('查询失败：参合证号格式不正确或不存在')
    } else {
      ElMessage.error(error.response?.data?.message || '查询失败')
    }
    return { code: error.response?.status || 500, msg: error.response?.data?.message || '查询失败' }
  }
}

// 新增参合人员
export async function addParticipationInfo(data) {
  try {
    const res = await request({
      url: '/insureds/add',
      method: 'post',
      data
    })
    return res
  } catch (error) {
    ElMessage.error(error.response?.data?.message || '添加失败')
    throw error
  }
}

// 更新参合人员信息
export async function updateParticipationInfo(data) {
  try {
    const res = await request({
      url: '/insureds/update',
      method: 'post',
      data
    })
    return res
  } catch (error) {
    ElMessage.error(error.response?.data?.message || '更新失败')
    throw error
  }
}

// 删除参合人员
export async function deleteParticipationInfo(id) {
  try {
    const res = await request({
      url: `/insureds/delete/${id}`,
      method: 'delete'
    })
    return res
  } catch (error) {
    ElMessage.error(error.response?.data?.message || '删除失败')
    throw error
  }
} 