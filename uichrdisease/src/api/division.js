import request from '@/utils/request'

// 获取行政区树形数据
export const getDivisionTree = async (name = '') => {
  try {
    const response = await request.get('/division/tree', { params: { name } })
    // 直接返回数据数组，因为后端返回的就是数组格式
    return response.data
  } catch (error) {
    throw error
  }
}

// 添加行政区
export const addDivision = async (data) => {
  try {
    const response = await request.post('/division/add', data)
    return response.data
  } catch (error) {
    throw error
  }
}

// 更新行政区
export const updateDivision = async (data) => {
  try {
    const response = await request.post('/division/update', data)
    return response.data
  } catch (error) {
    throw error
  }
}

// 删除行政区
export const deleteDivision = async (id) => {
  try {
    const response = await request.delete(`/division/delete/${id}`)
    return response.data
  } catch (error) {
    throw error
  }
} 