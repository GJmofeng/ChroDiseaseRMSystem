import request from '@/utils/request'

// 获取慢性病证列表
export function getChronicCertList(params) {
  return request({
    url: '/medicalCard/getAll',
    method: 'get',
    params: {
      fullname: params.insuredName || '',
      medicalName: params.medicalName || ''
    }
  })
}

// 根据卡号查询
export function getMedicalByCardId(cardId) {
  return request({
    url: `/medicalCard/getMedical/${cardId}`,
    method: 'get'
  })
}

// 新增慢性病证
export function addChronicCert(data) {
  return request({
    url: '/medicalCard/add',
    method: 'post',
    data
  })
}

// 更新慢性病证
export function updateChronicCert(data) {
  return request({
    url: '/medicalCard/update',
    method: 'post',
    data
  })
}

// 删除慢性病证
export function deleteChronicCert(id) {
  return request({
    url: `/medicalCard/delete/${id}`,
    method: 'delete'
  })
} 