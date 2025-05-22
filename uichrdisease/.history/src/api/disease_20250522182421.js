import request from '@/utils/request'

// 获取慢病信息列表
export function getDiseaseList(params) {
  return request({
    url: '/api/disease/list',
    method: 'get',
    params
  })
}

// 新增慢病信息
export function addDisease(data) {
  return request({
    url: '/api/disease',
    method: 'post',
    data
  })
}

// 更新慢病信息
export function updateDisease(data) {
  return request({
    url: '/api/disease',
    method: 'put',
    data
  })
}

// 删除慢病信息
export function deleteDisease(id) {
  return request({
    url: `/api/disease/${id}`,
    method: 'delete'
  })
}

// 获取慢病信息详情
export function getDiseaseDetail(id) {
  return request({
    url: `/api/disease/${id}`,
    method: 'get'
  })
} 