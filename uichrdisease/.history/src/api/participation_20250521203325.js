import request from '@/utils/request'

// 查询参合信息列表
export function searchParticipationInfo(params) {
  return request({
    url: '/participation/list',
    method: 'get',
    params
  })
}

// 新增参合信息
export function addParticipationInfo(data) {
  return request({
    url: '/participation',
    method: 'post',
    data
  })
}

// 更新参合信息
export function updateParticipationInfo(data) {
  return request({
    url: '/participation',
    method: 'put',
    data
  })
}

// 删除参合信息
export function deleteParticipationInfo(id) {
  return request({
    url: `/participation/${id}`,
    method: 'delete'
  })
} 