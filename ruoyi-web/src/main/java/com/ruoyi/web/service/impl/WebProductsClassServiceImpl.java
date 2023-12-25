package com.ruoyi.web.service.impl;

import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.domain.TreeSelect;
import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.entity.WebProductsClass;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.web.mapper.WebProductsClassMapper;
import com.ruoyi.web.mapper.WebProductsClassRoleMapper;
import com.ruoyi.web.service.IWebProductsClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 产品类别 服务实现
 *
 * @author ruoyi
 */
@Service
public class WebProductsClassServiceImpl implements IWebProductsClassService {
    @Autowired
    private WebProductsClassMapper productsClassMapper;

    @Autowired
    private WebProductsClassRoleMapper productsClassRoleMapper;

    /**
     * 查询产品类别数据
     *
     * @param productsClass 产品类别
     * @return 产品类别集合
     */
    @Override
    @DataScope(deptAlias = "d")
    public List<WebProductsClass> selectWebProductsClassList(WebProductsClass productsClass) {
        return productsClassMapper.selectWebProductsClassList(productsClass);
    }

    /**
     * 查询产品类别树结构信息
     *
     * @param productsClass 产品类别
     * @return 产品类别树信息集合
     */
    @Override
    public List<TreeSelect> selectProductsClassTreeList(WebProductsClass productsClass) {
        List<WebProductsClass> productsClasses = SpringUtils.getAopProxy(this).selectWebProductsClassList(productsClass);
        return buildProductsClassTreeSelect(productsClasses);
    }

    /**
     * 构建前端所需要树结构
     *
     * @param productsClasses 产品类别列表
     * @return 树结构列表
     */
    @Override
    public List<WebProductsClass> buildProductsClassTree(List<WebProductsClass> productsClasses) {
        List<WebProductsClass> returnList = new ArrayList<WebProductsClass>();
        List<Long> tempList = productsClasses.stream().map(WebProductsClass::getProductClassId).collect(Collectors.toList());
        for (WebProductsClass productClass : productsClasses) {
            // 如果是顶级节点, 遍历该父节点的所有子节点
            if (!tempList.contains(productClass.getParentId())) {
                recursionFn(productsClasses, productClass);
                returnList.add(productClass);
            }
        }
        if (returnList.isEmpty()) {
            returnList = productsClasses;
        }
        return returnList;
    }

    /**
     * 构建前端所需要下拉树结构
     *
     * @param productsClasses 产品类别列表
     * @return 下拉树结构列表
     */
    @Override
    public List<TreeSelect> buildProductsClassTreeSelect(List<WebProductsClass> productsClasses) {
        List<WebProductsClass> productClassTrees = buildProductsClassTree(productsClasses);
        return productClassTrees.stream().map(TreeSelect::new).collect(Collectors.toList());
    }

    /**
     * 根据角色ID查询产品类别树信息
     *
     * @param roleId 角色ID
     * @return 选中产品类别列表
     */
    @Override
    public List<Long> selectProductsClassListByRoleId(Long roleId) {
        SysRole role = productsClassRoleMapper.selectRoleById(roleId);
        return productsClassMapper.selectProductsClassListByRoleId(roleId, role.isDeptCheckStrictly());
    }

    /**
     * 根据产品类别ID查询信息
     *
     * @param productClassId 产品类别ID
     * @return 产品类别
     */
    @Override
    public WebProductsClass selectProductClassById(Long productClassId) {
        return productsClassMapper.selectProductClassById(productClassId);
    }

    /**
     * 根据ID查询所有子产品类别（正常状态）
     *
     * @param productClassId 产品类别ID
     * @return 子产品类别数
     */
    @Override
    public int selectNormalChildrenProductClassById(Long productClassId) {
        return productsClassMapper.selectNormalChildrenProductClassById(productClassId);
    }

    /**
     * 是否存在子节点
     *
     * @param productClassId 产品类别ID
     * @return 结果
     */
    @Override
    public boolean hasChildByProductClassId(Long productClassId) {
        int result = productsClassMapper.hasChildByProductClassId(productClassId);
        return result > 0;
    }

    /**
     * 查询产品类别是否存在对应产品
     *
     * @param productClassId 产品类别ID
     * @return 结果 true 存在 false 不存在
     */
    @Override
    public boolean checkProductsClassExistProduct(Long productClassId) {
        int result = productsClassMapper.checkProductsClassExistProduct(productClassId);
        return result > 0;
    }

    /**
     * 校验产品类别名称是否唯一
     *
     * @param productClass 产品类别
     * @return 结果
     */
    @Override
    public boolean checkProductsClassNameUnique(WebProductsClass productClass) {
        Long productClassId = StringUtils.isNull(productClass.getProductClassId()) ? -1L : productClass.getProductClassId();
        WebProductsClass info = productsClassMapper.checkProductsClassNameUnique(productClass.getProductClassName(),
                productClass.getParentId());
        if (StringUtils.isNotNull(info) && info.getProductClassId().longValue() != productClassId.longValue()) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校验产品类别是否有数据权限
     *
     * @param productClassId 产品类别id
     */
    @Override
    public void checkProductsClassDataScope(Long productClassId) {
        if (!SysUser.isAdmin(SecurityUtils.getUserId())) {
            WebProductsClass productClass = new WebProductsClass();
            productClass.setProductClassId(productClassId);
            List<WebProductsClass> productsClasses = SpringUtils.getAopProxy(this).selectWebProductsClassList(productClass);
            if (StringUtils.isEmpty(productsClasses)) {
                throw new ServiceException("没有权限访问产品类别数据！");
            }
        }
    }

    /**
     * 新增保存产品类别
     *
     * @param productClass 产品类别
     * @return 结果
     */
    @Override
    public int insertProductsClass(WebProductsClass productClass) {
        WebProductsClass info = productsClassMapper.selectProductClassById(productClass.getParentId());
        // 如果父节点不为正常状态,则不允许新增子节点
        if (!UserConstants.DEPT_NORMAL.equals(info.getStatus())) {
            throw new ServiceException("产品类别停用，不允许新增");
        }
        productClass.setAncestors(info.getAncestors() + "," + productClass.getParentId());
        return productsClassMapper.insertProductsClass(productClass);
    }

    /**
     * 修改保存产品类别
     *
     * @param productClass 产品类别
     * @return 结果
     */
    @Override
    public int updateProductsClass(WebProductsClass productClass) {
        WebProductsClass newParentProductsClass = productsClassMapper.selectProductClassById(productClass.getParentId());
        WebProductsClass oldProductsClass = productsClassMapper.selectProductClassById(productClass.getProductClassId());
        if (StringUtils.isNotNull(newParentProductsClass) && StringUtils.isNotNull(oldProductsClass)) {
            String newAncestors = newParentProductsClass.getAncestors() + "," + newParentProductsClass.getProductClassId();
            String oldAncestors = oldProductsClass.getAncestors();
            productClass.setAncestors(newAncestors);
            updateProductsClassChildren(productClass.getProductClassId(), newAncestors, oldAncestors);
        }
        int result = productsClassMapper.updateProductsClass(productClass);
        if (UserConstants.DEPT_NORMAL.equals(productClass.getStatus()) && StringUtils.isNotEmpty(productClass.getAncestors())
                && !StringUtils.equals("0", productClass.getAncestors())) {
            // 如果该产品类别是启用状态，则启用该产品类别的所有上级产品类别
            updateParentProductsClassStatusNormal(productClass);
        }
        return result;
    }

    /**
     * 修改该产品类别的父级产品类别状态
     *
     * @param productClass 当前产品类别
     */
    private void updateParentProductsClassStatusNormal(WebProductsClass productClass) {
        String ancestors = productClass.getAncestors();
        Long[] productClassIds = Convert.toLongArray(ancestors);
        productsClassMapper.updateProductsClassStatusNormal(productClassIds);
    }

    /**
     * 修改子元素关系
     *
     * @param productClassId       被修改的产品类别ID
     * @param newAncestors 新的父ID集合
     * @param oldAncestors 旧的父ID集合
     */
    public void updateProductsClassChildren(Long productClassId, String newAncestors, String oldAncestors) {
        List<WebProductsClass> children = productsClassMapper.selectChildrenProductClassById(productClassId);
        for (WebProductsClass child : children) {
            child.setAncestors(child.getAncestors().replaceFirst(oldAncestors, newAncestors));
        }
        if (children.size() > 0) {
            productsClassMapper.updateProductsClass(children);
        }
    }

    /**
     * 删除产品类别信息
     *
     * @param productClassId 产品类别ID
     * @return 结果
     */
    @Override
    public int deleteProductsClassById(Long productClassId) {
        return productsClassMapper.deleteProductsClassById(productClassId);
    }

    /**
     * 递归列表
     */
    private void recursionFn(List<WebProductsClass> list, WebProductsClass t) {
        // 得到子节点列表
        List<WebProductsClass> childList = getChildList(list, t);
        t.setChildren(childList);
        for (WebProductsClass tChild : childList) {
            if (hasChild(list, tChild)) {
                recursionFn(list, tChild);
            }
        }
    }

    /**
     * 得到子节点列表
     */
    private List<WebProductsClass> getChildList(List<WebProductsClass> list, WebProductsClass t) {
        List<WebProductsClass> tlist = new ArrayList<WebProductsClass>();
        Iterator<WebProductsClass> it = list.iterator();
        while (it.hasNext()) {
            WebProductsClass n = (WebProductsClass) it.next();
            if (StringUtils.isNotNull(n.getParentId()) && n.getParentId().longValue() == t.getProductClassId().longValue()) {
                tlist.add(n);
            }
        }
        return tlist;
    }

    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<WebProductsClass> list, WebProductsClass t) {
        return getChildList(list, t).size() > 0;
    }
}
