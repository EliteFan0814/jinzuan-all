package com.ruoyi.web.service;

import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.entity.WebProduct;

import java.util.List;

/**
 * 产品 业务层
 * 
 * @author ruoyi
 */
public interface IWebProductService
{
    /**
     * 根据条件分页查询产品列表
     * 
     * @param webProduct 产品信息
     * @return 产品信息集合信息
     */
    public List<WebProduct> selectProductList(WebProduct webProduct);

    /**
     * 通过产品名查询产品
     * 
     * @param webProductName 产品名
     * @return 产品对象信息
     */
    public WebProduct selectProductByProductName(String webProductName);
    /**
     * 通过产品英文名查询产品
     *
     * @param webProductNameEn 产品英文名
     * @return 产品对象信息
     */
    public WebProduct selectProductByProductNameEn(String webProductNameEn);

    /**
     * 通过产品ID查询产品
     * 
     * @param webProductId 产品ID
     * @return 产品对象信息
     */
    public WebProduct selectProductById(Long webProductId);

    /**
     * 校验产品名称是否唯一
     * 
     * @param webProduct 产品信息
     * @return 结果
     */
    public boolean checkProductNameUnique(WebProduct webProduct);

    /**
     * 新增产品信息
     * 
     * @param webProduct 产品信息
     * @return 结果
     */
    public int insertProduct(WebProduct webProduct);

    /**
     * 注册产品信息
     * 
     * @param webProduct 产品信息
     * @return 结果
     */
    public boolean registerProduct(WebProduct webProduct);

    /**
     * 修改产品信息
     * 
     * @param webProduct 产品信息
     * @return 结果
     */
    public int updateProduct(WebProduct webProduct);

    /**
     * 修改产品状态
     * 
     * @param webProduct 产品信息
     * @return 结果
     */
    public int updateProductStatus(WebProduct webProduct);

    /**
     * 修改产品基本信息
     * 
     * @param webProduct 产品信息
     * @return 结果
     */
    public int updateProductProfile(WebProduct webProduct);

    /**
     * 修改产品头像
     * 
     * @param webProductName 产品名
     * @param avatar 头像地址
     * @return 结果
     */
    public boolean updateProductAvatar(String webProductName, String avatar);

    /**
     * 通过产品ID删除产品
     * 
     * @param webProductId 产品ID
     * @return 结果
     */
    public int deleteProductById(Long webProductId);

    /**
     * 批量删除产品信息
     * 
     * @param webProductIds 需要删除的产品ID
     * @return 结果
     */
    public int deleteProductByIds(Long[] webProductIds);

    /**
     * 导入产品数据
     * 
     * @param webProductList 产品数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作产品
     * @return 结果
     */
    public String importProduct(List<WebProduct> webProductList, Boolean isUpdateSupport, String operName);
}
