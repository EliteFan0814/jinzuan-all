import request from '@/utils/request'

// 查询部门列表
export function listProductsClass(query) {
  return request({
    url: '/web/productsClass/list',
    method: 'get',
    params: query
  })
}

// 查询部门列表（排除节点）
export function listProductsClassExcludeChild(productsClassId) {
  return request({
    url: '/web/productsClass/list/exclude/' + productsClassId,
    method: 'get'
  })
}

// 查询部门详细
export function getProductsClass(productsClassId) {
  return request({
    url: '/web/productsClass/' + productsClassId,
    method: 'get'
  })
}

// 新增部门
export function addProductsClass(data) {
  return request({
    url: '/web/productsClass',
    method: 'post',
    data: data
  })
}

// 修改部门
export function updateProductsClass(data) {
  return request({
    url: '/web/productsClass',
    method: 'put',
    data: data
  })
}

// 删除部门
export function delProductsClass(productsClassId) {
  return request({
    url: '/web/productsClass/' + productsClassId,
    method: 'delete'
  })
}