package com.ruoyi.web.service.impl;

import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.domain.TreeSelect;
import com.ruoyi.common.core.domain.entity.SysDept;
import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.entity.WebProductsClass;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.system.mapper.SysDeptMapper;
import com.ruoyi.system.mapper.SysRoleMapper;
import com.ruoyi.system.service.ISysDeptService;
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
        List<WebProductsClass> depts = SpringUtils.getAopProxy(this).selectWebProductsClassList(productsClass);
        return buildProductsClassTreeSelect(depts);
    }

    /**
     * 构建前端所需要树结构
     *
     * @param depts 产品类别列表
     * @return 树结构列表
     */
    @Override
    public List<WebProductsClass> buildProductsClassTree(List<WebProductsClass> depts) {
        List<WebProductsClass> returnList = new ArrayList<WebProductsClass>();
        List<Long> tempList = depts.stream().map(WebProductsClass::getProductClassId).collect(Collectors.toList());
        for (WebProductsClass dept : depts) {
            // 如果是顶级节点, 遍历该父节点的所有子节点
            if (!tempList.contains(dept.getParentId())) {
                recursionFn(depts, dept);
                returnList.add(dept);
            }
        }
        if (returnList.isEmpty()) {
            returnList = depts;
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
        List<WebProductsClass> deptTrees = buildProductsClassTree(productsClasses);
        return deptTrees.stream().map(TreeSelect::new).collect(Collectors.toList());
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
        return productsClassMapper.selectDeptListByRoleId(roleId, role.isDeptCheckStrictly());
    }

    /**
     * 根据产品类别ID查询信息
     *
     * @param deptId 产品类别ID
     * @return 产品类别
     */
    @Override
    public WebProductsClass selectProductClassById(Long deptId) {
        return productsClassMapper.selectProductClassById(deptId);
    }

    /**
     * 根据ID查询所有子产品类别（正常状态）
     *
     * @param deptId 产品类别ID
     * @return 子产品类别数
     */
    @Override
    public int selectNormalChildrenProductClassById(Long deptId) {
        return productsClassMapper.selectNormalChildrenProductClassById(deptId);
    }

    /**
     * 是否存在子节点
     *
     * @param deptId 产品类别ID
     * @return 结果
     */
    @Override
    public boolean hasChildByProductClassId(Long deptId) {
        int result = productsClassMapper.hasChildByProductClassId(deptId);
        return result > 0;
    }

    /**
     * 查询产品类别是否存在对应产品
     *
     * @param deptId 产品类别ID
     * @return 结果 true 存在 false 不存在
     */
    @Override
    public boolean checkProductsClassExistProduct(Long deptId) {
        int result = productsClassMapper.checkProductsClassExistProduct(deptId);
        return result > 0;
    }

    /**
     * 校验产品类别名称是否唯一
     *
     * @param dept 产品类别
     * @return 结果
     */
    @Override
    public boolean checkProductsClassNameUnique(WebProductsClass dept) {
        Long deptId = StringUtils.isNull(dept.getProductClassId()) ? -1L : dept.getProductClassId();
        WebProductsClass info = productsClassMapper.checkProductsClassNameUnique(dept.getProductClassName(),
                dept.getParentId());
        if (StringUtils.isNotNull(info) && info.getProductClassId().longValue() != deptId.longValue()) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校验产品类别是否有数据权限
     *
     * @param deptId 产品类别id
     */
    @Override
    public void checkProductsClassDataScope(Long deptId) {
        if (!SysUser.isAdmin(SecurityUtils.getUserId())) {
            WebProductsClass dept = new WebProductsClass();
            dept.setProductClassId(deptId);
            List<WebProductsClass> depts = SpringUtils.getAopProxy(this).selectWebProductsClassList(dept);
            if (StringUtils.isEmpty(depts)) {
                throw new ServiceException("没有权限访问产品类别数据！");
            }
        }
    }

    /**
     * 新增保存产品类别
     *
     * @param dept 产品类别
     * @return 结果
     */
    @Override
    public int insertProductsClass(WebProductsClass dept) {
        WebProductsClass info = productsClassMapper.selectProductClassById(dept.getParentId());
        // 如果父节点不为正常状态,则不允许新增子节点
        if (!UserConstants.DEPT_NORMAL.equals(info.getStatus())) {
            throw new ServiceException("产品类别停用，不允许新增");
        }
        dept.setAncestors(info.getAncestors() + "," + dept.getParentId());
        return productsClassMapper.insertProductsClass(dept);
    }

    /**
     * 修改保存产品类别
     *
     * @param dept 产品类别
     * @return 结果
     */
    @Override
    public int updateProductsClass(WebProductsClass dept) {
        WebProductsClass newParentDept = productsClassMapper.selectProductClassById(dept.getParentId());
        WebProductsClass oldDept = productsClassMapper.selectProductClassById(dept.getProductClassId());
        if (StringUtils.isNotNull(newParentDept) && StringUtils.isNotNull(oldDept)) {
            String newAncestors = newParentDept.getAncestors() + "," + newParentDept.getProductClassId();
            String oldAncestors = oldDept.getAncestors();
            dept.setAncestors(newAncestors);
            updateDeptChildren(dept.getProductClassId(), newAncestors, oldAncestors);
        }
        int result = productsClassMapper.updateProductsClass(dept);
        if (UserConstants.DEPT_NORMAL.equals(dept.getStatus()) && StringUtils.isNotEmpty(dept.getAncestors())
                && !StringUtils.equals("0", dept.getAncestors())) {
            // 如果该产品类别是启用状态，则启用该产品类别的所有上级产品类别
            updateParentDeptStatusNormal(dept);
        }
        return result;
    }

    /**
     * 修改该产品类别的父级产品类别状态
     *
     * @param dept 当前产品类别
     */
    private void updateParentDeptStatusNormal(WebProductsClass dept) {
        String ancestors = dept.getAncestors();
        Long[] deptIds = Convert.toLongArray(ancestors);
        productsClassMapper.updateDeptStatusNormal(deptIds);
    }

    /**
     * 修改子元素关系
     *
     * @param deptId       被修改的产品类别ID
     * @param newAncestors 新的父ID集合
     * @param oldAncestors 旧的父ID集合
     */
    public void updateDeptChildren(Long deptId, String newAncestors, String oldAncestors) {
        List<WebProductsClass> children = productsClassMapper.selectChildrenProductClassById(deptId);
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
     * @param deptId 产品类别ID
     * @return 结果
     */
    @Override
    public int deleteProductsClassById(Long deptId) {
        return productsClassMapper.deleteProductsClassById(deptId);
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
