package com.ruoyi.web.service;

import com.ruoyi.common.core.domain.entity.WebNews;

import java.util.List;

/**
 * 新闻 业务层
 * 
 * @author ruoyi
 */
public interface IWebNewsService
{
    /**
     * 根据条件分页查询新闻列表
     * 
     * @param webNews 新闻信息
     * @return 新闻信息集合信息
     */
    public List<WebNews> selectNewsList(WebNews webNews);

    /**
     * 通过新闻名查询新闻
     * 
     * @param webNewsName 新闻名
     * @return 新闻对象信息
     */
    public WebNews selectNewsByNewsName(String webNewsName);
    /**
     * 通过新闻英文名查询新闻
     *
     * @param webNewsNameEn 新闻英文名
     * @return 新闻对象信息
     */
    public WebNews selectNewsByNewsNameEn(String webNewsNameEn);

    /**
     * 通过新闻ID查询新闻
     * 
     * @param webNewsId 新闻ID
     * @return 新闻对象信息
     */
    public WebNews selectNewsById(Long webNewsId);

    /**
     * 校验新闻名称是否唯一
     * 
     * @param webNews 新闻信息
     * @return 结果
     */
    public boolean checkNewsNameUnique(WebNews webNews);

    /**
     * 新增新闻信息
     * 
     * @param webNews 新闻信息
     * @return 结果
     */
    public int insertNews(WebNews webNews);

    /**
     * 注册新闻信息
     * 
     * @param webNews 新闻信息
     * @return 结果
     */
    public boolean registerNews(WebNews webNews);

    /**
     * 修改新闻信息
     * 
     * @param webNews 新闻信息
     * @return 结果
     */
    public int updateNews(WebNews webNews);

    /**
     * 修改新闻状态
     * 
     * @param webNews 新闻信息
     * @return 结果
     */
    public int updateNewsStatus(WebNews webNews);

    /**
     * 修改新闻基本信息
     * 
     * @param webNews 新闻信息
     * @return 结果
     */
    public int updateNewsProfile(WebNews webNews);

    /**
     * 修改新闻头像
     * 
     * @param webNewsName 新闻名
     * @param avatar 头像地址
     * @return 结果
     */
    public boolean updateNewsAvatar(String webNewsName, String avatar);

    /**
     * 通过新闻ID删除新闻
     * 
     * @param webNewsId 新闻ID
     * @return 结果
     */
    public int deleteNewsById(Long webNewsId);

    /**
     * 批量删除新闻信息
     * 
     * @param webNewsIds 需要删除的新闻ID
     * @return 结果
     */
    public int deleteNewsByIds(Long[] webNewsIds);

    /**
     * 导入新闻数据
     * 
     * @param webNewsList 新闻数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作新闻
     * @return 结果
     */
    public String importNews(List<WebNews> webNewsList, Boolean isUpdateSupport, String operName);
}
