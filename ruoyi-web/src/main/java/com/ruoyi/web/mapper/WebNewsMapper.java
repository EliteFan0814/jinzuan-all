package com.ruoyi.web.mapper;

import com.ruoyi.common.core.domain.entity.WebNews;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 新闻表 数据层
 * 
 * @author fanpeichao
 */
public interface WebNewsMapper
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
     * @param newsName 新闻名
     * @return 新闻对象信息
     */
    public WebNews selectNewsByNewsName(String newsName);
    /**
     * 通过新闻英文名查询新闻
     *
     * @param newsNameEn 新闻英文名
     * @return 新闻对象信息
     */
    public WebNews selectNewsByNewsNameEn(String newsNameEn);

    /**
     * 通过新闻ID查询新闻
     * 
     * @param newsId 新闻ID
     * @return 新闻对象信息
     */
    public WebNews selectNewsById(Long newsId);

    /**
     * 新增新闻信息
     * 
     * @param news 新闻信息
     * @return 结果
     */
    public int insertNews(WebNews news);

    /**
     * 修改新闻信息
     * 
     * @param news 新闻信息
     * @return 结果
     */
    public int updateNews(WebNews news);

    /**
     * 修改新闻头像
     * 
     * @param newsName 新闻名
     * @param avatar 头像地址
     * @return 结果
     */
    public int updateNewsAvatar(@Param("newsName") String newsName, @Param("avatar") String avatar);

    /**
     * 通过新闻ID删除新闻
     * 
     * @param newsId 新闻ID
     * @return 结果
     */
    public int deleteNewsById(Long newsId);

    /**
     * 批量删除新闻信息
     * 
     * @param newsIds 需要删除的新闻ID
     * @return 结果
     */
    public int deleteNewsByIds(Long[] newsIds);

    /**
     * 校验新闻名称是否唯一
     * 
     * @param newsName 新闻名称
     * @return 结果
     */
    public WebNews checkNewsNameUnique(String newsName);
}
