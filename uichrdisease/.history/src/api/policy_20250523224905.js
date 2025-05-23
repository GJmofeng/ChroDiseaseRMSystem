import request from '@/utils/request'

// 获取政策列表
export function getPolicyList() {
  return request({
    url: '/api/policy/list',
    method: 'get'
  })
}

// 新增政策
export function addPolicy(data) {
  return request({
    url: '/api/policy',
    method: 'post',
    data
  })
}

// 更新政策
export function updatePolicy(data) {
  return request({
    url: '/api/policy',
    method: 'put',
    data
  })
}

// 删除政策
export function deletePolicy(id) {
  return request({
    url: `/api/policy/${id}`,
    method: 'delete'
  })
}

// 根据年度查询政策
export function getPolicyByYear(year) {
  return request({
    url: `/api/policy/year/${year}`,
    method: 'get'
  })
} 