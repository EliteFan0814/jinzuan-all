package com.ruoyi.web.mapper;

import com.ruoyi.common.core.domain.entity.SysDept;
import com.ruoyi.common.core.domain.entity.WebProductsClass;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 产品类别管理 数据层
 * 
 * @author ruoyi
 */
public interface WebProductsClassMapper
{
    /**
     * 查询产品类别管理数据
     * 
     * @param productsClass 产品类别信息
     * @return 产品类别信息集合
     */
    public List<WebProductsClass> selectWebProductsClassList(WebProductsClass productsClass);

    /**
     * 根据角色ID查询产品类别树信息[暂时不用]
     * 
     * @param roleId 角色ID
     * @param productClassCheckStrictly 产品类别树选择项是否关联显示
     * @return 选中产品类别列表
     */
    public List<Long> selectProductsClassListByRoleId(@Param("roleId") Long roleId, @Param("productClassCheckStrictly") boolean productClassCheckStrictly);

    /**
     * 根据产品类别ID查询信息
     * 
     * @param productClassId 产品类别ID
     * @return 产品类别信息
     */
    public WebProductsClass selectProductClassById(Long productClassId);

    /**
     * 根据ID查询所有子产品类别
     * 
     * @param productClassId 产品类别ID
     * @return 产品类别列表
     */
    public List<WebProductsClass> selectChildrenProductClassById(Long productClassId);

    /**
     * 根据ID查询所有子产品类别（正常状态）
     * 
     * @param productClassId 产品类别ID
     * @return 子产品类别数
     */
    public int selectNormalChildrenProductClassById(Long productClassId);

    /**
     * 是否存在子节点
     * 
     * @param productClassId 产品类别ID
     * @return 结果
     */
    public int hasChildByProductClassId(Long productClassId);

    /**
     * 查询产品类别是否存在产品
     * 
     * @param productClassId 产品类别ID
     * @return 结果
     */
    public int checkProductsClassExistProduct(Long productClassId);

    /**
     * 校验产品类别名称是否唯一
     * 
     * @param productClassName 产品类别名称
     * @param parentId 父产品类别ID
     * @return 结果
     */
    public WebProductsClass checkProductsClassNameUnique(@Param("productClassName") String productClassName, @Param("parentId") Long parentId);

    /**
     * 新增产品类别信息
     * 
     * @param productClass 产品类别信息
     * @return 结果
     */
    public int insertProductsClass(WebProductsClass productClass);

    /**
     * 修改产品类别信息
     * 
     * @param productClass 产品类别信息
     * @return 结果
     */
    public int updateProductsClass(WebProductsClass productClass);

    /**
     * 修改所在产品类别正常状态
     * 
     * @param productClassIds 产品类别ID组
     */
    public void updateProductsClassStatusNormal(Long[] productClassIds);

    /**
     * 修改子元素关系
     * 
     * @param productClasses 子元素
     * @return 结果
     */
    public int updateProductsClass(@Param("productClasses") List<WebProductsClass> productClasses);

    /**
     * 删除产品类别管理信息
     * 
     * @param productClassId 产品类别ID
     * @return 结果
     */
    public int deleteProductsClassById(Long productClassId);
}
