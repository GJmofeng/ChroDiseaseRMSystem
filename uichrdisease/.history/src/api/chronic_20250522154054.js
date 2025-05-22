import request from '@/utils/request'

// 获取慢性病证列表
export function getChronicCertList(params) {
  return request({
    url: '/medical-card/list',
    method: 'get',
    params
  })
}

// 新增慢性病证
export function addChronicCert(data) {
  return request({
    url: '/medical-card/add',
    method: 'post',
    data
  })
}

// 更新慢性病证
export function updateChronicCert(data) {
  return request({
    url: '/medical-card/update',
    method: 'put',
    data
  })
}

// 删除慢性病证
export function deleteChronicCert(id) {
  return request({
    url: `/medical-card/delete/${id}`,
    method: 'delete'
  })
} 