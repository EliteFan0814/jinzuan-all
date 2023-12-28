package com.ruoyi.web.service;

import com.ruoyi.common.core.domain.TreeSelect;
import com.ruoyi.common.core.domain.entity.SysDept;
import com.ruoyi.common.core.domain.entity.WebProductsClass;

import java.util.List;

/**
 * 产品类管理 服务层
 *
 * @author fanpeichao
 */
public interface IWebProductsClassService {
    /**
     * 查询产品类别数据
     *
     * @param productsClass 产品类别信息
     * @return 产品类别集合
     */
    public List<WebProductsClass> selectWebProductsClassList(WebProductsClass productsClass);

    /**
     * 查询产品类别树结构信息
     *
     * @param productsClass 产品类别
     * @return 产品类别树信息集合
     */
    public List<TreeSelect> selectProductsClassTreeList(WebProductsClass productsClass);

    /**
     * 构建前端所需要树结构
     *
     * @param productsClasses 产品类别列表
     * @return 树结构列表
     */
    public List<WebProductsClass> buildProductsClassTree(List<WebProductsClass> productsClasses);

    /**
     * 构建前端所需要下拉树结构
     *
     * @param productsClasses 产品类别列表
     * @return 下拉树结构列表
     */
    public List<TreeSelect> buildProductsClassTreeSelect(List<WebProductsClass> productsClasses);

    /**
     * 根据产品类别ID查询信息
     *
     * @param productClassId 产品类别ID
     * @return 类别信息
     */
    public WebProductsClass selectProductClassById(Long productClassId);

    /**
     * 根据ID查询所有子类别（正常状态）
     *
     * @param productClassId 产品类别ID
     * @return 子类别数
     */
    public int selectNormalChildrenProductClassById(Long productClassId);

    /**
     * 是否存在类别子节点
     *
     * @param productClassId 类别ID
     * @return 结果
     */
    public boolean hasChildByProductClassId(Long productClassId);

    /**
     * 查询产品类别是否存在对应产品
     *
     * @param productClassId 产品类别ID
     * @return 结果 true 存在 false 不存在
     */
    public boolean checkProductsClassExistProduct(Long productClassId);

    /**
     * 校验产品类别名称是否唯一
     *
     * @param productsClass 产品类别信息
     * @return 结果
     */
    public boolean checkProductsClassNameUnique(WebProductsClass productsClass);

    /**
     * 校验产品类别是否有数据权限
     *
     * @param productClassId 产品类别id
     */
    public void checkProductsClassDataScope(Long productClassId);

    /**
     * 新增保存产品类别信息
     *
     * @param productsClass 产品类别信息
     * @return 结果
     */
    public int insertProductsClass(WebProductsClass productsClass);

    /**
     * 修改保存产品类别信息
     *
     * @param productsClass 产品类别信息
     * @return 结果
     */
    public int updateProductsClass(WebProductsClass productsClass);

    /**
     * 删除产品类别信息
     *
     * @param productClassId 产品类别ID
     * @return 结果
     */
    public int deleteProductsClassById(Long productClassId);
}
