import request from '@/utils/request'
import { ElMessage } from 'element-plus'

// 查询参合人员列表
export async function searchParticipationInfo({ pageNum = 1, pageSize = 10 } = {}) {
  try {
    const res = await request({
      url: '/insureds/page',
      method: 'get',
      params: {
        pageNum,
        pageSize
      }
    })
    return res
  } catch (error) {
    if (error.response?.status === 500) {
      ElMessage.error('服务器内部错误，请确保后端服务正常运行')
    } else {
      ElMessage.error(error.response?.data?.message || '获取数据失败')
    }
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