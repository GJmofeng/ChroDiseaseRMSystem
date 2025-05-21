import request from '@/utils/request'

// 查询参合人员列表
export function searchParticipationInfo(params) {
  return request({
    url: '/insureds/getAll',
    method: 'get'
  })
}

// 根据参合证号查询参合人员
export function getInsuredByCardId(cardId) {
  return request({
    url: `/insureds/getInsured/${cardId}`,
    method: 'get'
  })
}

// 新增参合人员
export function addParticipationInfo(data) {
  return request({
    url: '/insureds/add',
    method: 'post',
    data
  })
}

// 更新参合人员信息
export function updateParticipationInfo(data) {
  return request({
    url: '/insureds/update',
    method: 'post',
    data
  })
}

// 删除参合人员
export function deleteParticipationInfo(id) {
  return request({
    url: `/insureds/delete/${id}`,
    method: 'delete'
  })
} 