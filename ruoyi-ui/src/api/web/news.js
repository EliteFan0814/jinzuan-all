import request from "@/utils/request";
import { parseStrEmpty } from "@/utils/ruoyi";

// 查询新闻列表
export function listNews(query) {
  return request({
    url: "/web/news/list",
    method: "get",
    params: query,
  });
}

// 查询新闻详细
export function getNews(newsId) {
  return request({
    url: "/web/news/" + parseStrEmpty(newsId),
    method: "get",
  });
}

// 图片上传
export function upImg(data) {
  return request({
    url: "/common/upload",
    headers: { "Content-Type": "multipart/form-data" },
    method: "post",
    data: data,
  });
}
// 新增新闻
export function addNews(data) {
  return request({
    url: "/web/news",
    method: "post",
    data: data,
  });
}

// 修改新闻
export function updateNews(data) {
  return request({
    url: "/web/news",
    method: "put",
    data: data,
  });
}

// 删除新闻
export function delNews(newsId) {
  return request({
    url: "/web/news/" + newsId,
    method: "delete",
  });
}

// 新闻密码重置
export function resetNewsPwd(newsId, password) {
  const data = {
    newsId,
    password,
  };
  return request({
    url: "/web/news/resetPwd",
    method: "put",
    data: data,
  });
}

// 新闻状态修改
export function changeNewsStatus(newsId, status) {
  const data = {
    newsId,
    status,
  };
  return request({
    url: "/web/news/changeStatus",
    method: "put",
    data: data,
  });
}

// 查询新闻个人信息
export function getNewsProfile() {
  return request({
    url: "/web/news/profile",
    method: "get",
  });
}

// 修改新闻个人信息
export function updateNewsProfile(data) {
  return request({
    url: "/web/news/profile",
    method: "put",
    data: data,
  });
}

// 新闻密码重置
export function updateNewsPwd(oldPassword, newPassword) {
  const data = {
    oldPassword,
    newPassword,
  };
  return request({
    url: "/web/news/profile/updatePwd",
    method: "put",
    params: data,
  });
}

// 新闻头像上传
export function uploadAvatar(data) {
  return request({
    url: "/web/news/profile/avatar",
    method: "post",
    data: data,
  });
}

// 查询授权角色
export function getAuthRole(newsId) {
  return request({
    url: "/web/news/authRole/" + newsId,
    method: "get",
  });
}

// 保存授权角色
export function updateAuthRole(data) {
  return request({
    url: "/web/news/authRole",
    method: "put",
    params: data,
  });
}
