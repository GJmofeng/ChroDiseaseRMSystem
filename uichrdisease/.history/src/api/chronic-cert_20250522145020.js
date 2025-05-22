import request from '@/utils/request'

// 获取慢性病证列表
export function getChronicCertList(params) {
  return request({
    url: '/chronic-cert/list',
    method: 'get',
    params
  })
}

// 获取慢性病证详情
export function getChronicCertDetail(id) {
  return request({
    url: `/chronic-cert/${id}`,
    method: 'get'
  })
}

// 新增慢性病证
export function addChronicCert(data) {
  return request({
    url: '/chronic-cert',
    method: 'post',
    data
  })
}

// 更新慢性病证
export function updateChronicCert(id, data) {
  return request({
    url: `/chronic-cert/${id}`,
    method: 'put',
    data
  })
}

// 删除慢性病证
export function deleteChronicCert(id) {
  return request({
    url: `/chronic-cert/${id}`,
    method: 'delete'
  })
} 