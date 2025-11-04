import request from '@/utils/request'

// 查询指标列表
export function listDictionary(query) {
  return request({
    url: '/system/dictionary/list',
    method: 'get',
    params: query
  })
}

// 查询指标详细
export function getDictionary(id) {
  return request({
    url: '/system/dictionary/' + id,
    method: 'get'
  })
}

// 新增指标
export function addDictionary(data) {
  return request({
    url: '/system/dictionary',
    method: 'post',
    data: data
  })
}

// 修改指标
export function updateDictionary(data) {
  return request({
    url: '/system/dictionary',
    method: 'put',
    data: data
  })
}

// 删除指标
export function delDictionary(id) {
  return request({
    url: '/system/dictionary/' + id,
    method: 'delete'
  })
}
