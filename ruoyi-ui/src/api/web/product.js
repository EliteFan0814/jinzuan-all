import request from '@/utils/request'
import { parseStrEmpty } from "@/utils/ruoyi";

// 查询产品列表
export function listUser(query) {
  return request({
    url: '/web/product/list',
    method: 'get',
    params: query
  })
}

// 查询产品详细
export function getUser(userId) {
  return request({
    url: '/web/product/' + parseStrEmpty(userId),
    method: 'get'
  })
}

// 新增产品
export function addUser(data) {
  return request({
    url: '/web/product',
    method: 'post',
    data: data
  })
}

// 修改产品
export function updateUser(data) {
  return request({
    url: '/web/product',
    method: 'put',
    data: data
  })
}

// 删除产品
export function delUser(userId) {
  return request({
    url: '/web/product/' + userId,
    method: 'delete'
  })
}

// 产品密码重置
export function resetUserPwd(userId, password) {
  const data = {
    userId,
    password
  }
  return request({
    url: '/web/product/resetPwd',
    method: 'put',
    data: data
  })
}

// 产品状态修改
export function changeUserStatus(userId, status) {
  const data = {
    userId,
    status
  }
  return request({
    url: '/web/product/changeStatus',
    method: 'put',
    data: data
  })
}

// 查询产品个人信息
export function getUserProfile() {
  return request({
    url: '/web/product/profile',
    method: 'get'
  })
}

// 修改产品个人信息
export function updateUserProfile(data) {
  return request({
    url: '/web/product/profile',
    method: 'put',
    data: data
  })
}

// 产品密码重置
export function updateUserPwd(oldPassword, newPassword) {
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
export function getAuthRole(userId) {
  return request({
    url: '/web/product/authRole/' + userId,
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
export function deptTreeSelect() {
  return request({
    url: '/web/product/productsClassTree',
    method: 'get'
  })
}
