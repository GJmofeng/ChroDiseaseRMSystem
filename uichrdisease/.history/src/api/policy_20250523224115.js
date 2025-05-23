import request from '@/utils/request'

// 获取政策列表
export function getPolicyList() {
  return request({
    url: '/policy/list',
    method: 'get'
  })
}

// 新增政策
export function addPolicy(data) {
  return request({
    url: '/policy/add',
    method: 'post',
    data
  })
}

// 更新政策
export function updatePolicy(data) {
  return request({
    url: '/policy/update',
    method: 'put',
    data
  })
}

// 删除政策
export function deletePolicy(id) {
  return request({
    url: `/policy/delete/${id}`,
    method: 'delete'
  })
}

// 根据年度查询政策
export function getPolicyByYear(year) {
  return request({
    url: `/policy/getyear/${year}`,
    method: 'get'
  })
} 