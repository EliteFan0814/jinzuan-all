package com.ruoyi.web.mapper;

import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.entity.WebProduct;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 产品表 数据层
 * 
 * @author fanpeichao
 */
public interface WebProductMapper
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
     * @param productName 产品名
     * @return 产品对象信息
     */
    public WebProduct selectProductByProductName(String productName);
    /**
     * 通过产品英文名查询产品
     *
     * @param productNameEn 产品英文名
     * @return 产品对象信息
     */
    public WebProduct selectProductByProductNameEn(String productNameEn);

    /**
     * 通过产品ID查询产品
     * 
     * @param productId 产品ID
     * @return 产品对象信息
     */
    public WebProduct selectProductById(Long productId);

    /**
     * 新增产品信息
     * 
     * @param product 产品信息
     * @return 结果
     */
    public int insertProduct(WebProduct product);

    /**
     * 修改产品信息
     * 
     * @param product 产品信息
     * @return 结果
     */
    public int updateProduct(WebProduct product);

    /**
     * 修改产品头像
     * 
     * @param productName 产品名
     * @param avatar 头像地址
     * @return 结果
     */
    public int updateProductAvatar(@Param("productName") String productName, @Param("avatar") String avatar);

    /**
     * 通过产品ID删除产品
     * 
     * @param productId 产品ID
     * @return 结果
     */
    public int deleteProductById(Long productId);

    /**
     * 批量删除产品信息
     * 
     * @param productIds 需要删除的产品ID
     * @return 结果
     */
    public int deleteProductByIds(Long[] productIds);

    /**
     * 校验产品名称是否唯一
     * 
     * @param productName 产品名称
     * @return 结果
     */
    public WebProduct checkProductNameUnique(String productName);
}
