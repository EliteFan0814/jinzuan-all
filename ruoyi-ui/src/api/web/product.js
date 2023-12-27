import request from '@/utils/request'
import { parseStrEmpty } from "@/utils/ruoyi";

// 查询产品列表
export function listProduct(query) {
  return request({
    url: '/web/product/list',
    method: 'get',
    params: query
  })
}

// 查询产品详细
export function getProduct(productId) {
  return request({
    url: '/web/product/' + parseStrEmpty(productId),
    method: 'get'
  })
}

// 新增产品
export function addProduct(data) {
  return request({
    url: '/web/product',
    method: 'post',
    data: data
  })
}

// 修改产品
export function updateProduct(data) {
  return request({
    url: '/web/product',
    method: 'put',
    data: data
  })
}

// 删除产品
export function delProduct(productId) {
  return request({
    url: '/web/product/' + productId,
    method: 'delete'
  })
}

// 产品密码重置
export function resetProductPwd(productId, password) {
  const data = {
    productId,
    password
  }
  return request({
    url: '/web/product/resetPwd',
    method: 'put',
    data: data
  })
}

// 产品状态修改
export function changeProductStatus(productId, status) {
  const data = {
    productId,
    status
  }
  return request({
    url: '/web/product/changeStatus',
    method: 'put',
    data: data
  })
}

// 查询产品个人信息
export function getProductProfile() {
  return request({
    url: '/web/product/profile',
    method: 'get'
  })
}

// 修改产品个人信息
export function updateProductProfile(data) {
  return request({
    url: '/web/product/profile',
    method: 'put',
    data: data
  })
}

// 产品密码重置
export function updateProductPwd(oldPassword, newPassword) {
  const data = {
    oldPassword,
    newPassword
  }
  return request({
    url: '/web/product/profile/updatePwd',
    method: 'put',
    params: data
  })
}

// 产品头像上传
export function uploadAvatar(data) {
  return request({
    url: '/web/product/profile/avatar',
    method: 'post',
    data: data
  })
}

// 查询授权角色
export function getAuthRole(productId) {
  return request({
    url: '/web/product/authRole/' + productId,
    method: 'get'
  })
}

// 保存授权角色
export function updateAuthRole(data) {
  return request({
    url: '/web/product/authRole',
    method: 'put',
    params: data
  })
}

// 查询部门下拉树结构
export function productsClassTreeSelect() {
  return request({
    url: '/web/product/productsClassTree',
    method: 'get'
  })
}
