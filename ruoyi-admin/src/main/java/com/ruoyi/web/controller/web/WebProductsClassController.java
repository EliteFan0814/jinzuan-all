package com.ruoyi.web.controller.web;

import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.WebProductsClass;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.web.service.IWebProductsClassService;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 产品类别信息
 *
 * @author fanpeichao
 */
@RestController
@RequestMapping("/web/productsClass")
public class WebProductsClassController extends BaseController {
    @Autowired
    private IWebProductsClassService productsClassService;

    /**
     * 获取产品类别列表
     */
    @PreAuthorize("@ss.hasPermi('web:productsClass:list')")
    @GetMapping("/list")
    public AjaxResult list(WebProductsClass productsClass) {
        List<WebProductsClass> productsClasses = productsClassService.selectWebProductsClassList(productsClass);
        return success(productsClasses);
    }

    /**
     * 查询产品分类列表（排除当前产品类节点）
     */
    @PreAuthorize("@ss.hasPermi('web:productsClass:list')")
    @GetMapping("/list/exclude/{productsClassId}")
    public AjaxResult excludeChild(@PathVariable(value = "productsClassId", required = false) Long productsClassId) {
        List<WebProductsClass> productsClasses =
                productsClassService.selectWebProductsClassList(new WebProductsClass());
        productsClasses.removeIf(d -> d.getProductClassId().intValue() == productsClassId || ArrayUtils.contains(StringUtils.split(d.getAncestors(), ","), productsClassId + ""));
        return success(productsClasses);
    }

    /**
     * 根据产品分类编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('web:productsClass:query')")
    @GetMapping(value = "/{productsClassId}")
    public AjaxResult getInfo(@PathVariable Long productsClassId) {
        productsClassService.checkProductsClassDataScope(productsClassId);
        return success(productsClassService.selectProductClassById(productsClassId));
    }

    /**
     * 新增产品分类
     */
    @PreAuthorize("@ss.hasPermi('web:productsClass:add')")
    @Log(title = "产品分类管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody WebProductsClass productsClass) {
        if (!productsClassService.checkProductsClassNameUnique(productsClass)) {
            return error("新增产品分类'" + productsClass.getProductClassName() + "'失败，产品分类名称已存在");
        }
        productsClass.setCreateBy(getUsername());
        return toAjax(productsClassService.insertProductsClass(productsClass));
    }

    /**
     * 修改产品分类
     */
    @PreAuthorize("@ss.hasPermi('web:productsClass:edit')")
    @Log(title = "产品分类管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody WebProductsClass productsClass) {
        Long productsClassId = productsClass.getProductClassId();
        productsClassService.checkProductsClassDataScope(productsClassId);
        if (!productsClassService.checkProductsClassNameUnique(productsClass)) {
            return error("修改产品分类'" + productsClass.getProductClassName() + "'失败，产品分类名称已存在");
        } else if (productsClass.getParentId().equals(productsClassId)) {
            return error("修改产品分类'" + productsClass.getProductClassName() + "'失败，上级产品分类不能是自己");
        } else if (StringUtils.equals(UserConstants.DEPT_DISABLE, productsClass.getStatus()) && productsClassService.selectNormalChildrenProductClassById(productsClassId) > 0) {
            return error("该产品分类包含未停用的子产品分类！");
        }
        productsClass.setUpdateBy(getUsername());
        return toAjax(productsClassService.updateProductsClass(productsClass));
    }

    /**
     * 删除产品分类
     */
    @PreAuthorize("@ss.hasPermi('web:productsClass:remove')")
    @Log(title = "产品分类管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{productsClassId}")
    public AjaxResult remove(@PathVariable Long productsClassId) {
        if (productsClassService.hasChildByProductClassId(productsClassId)) {
            return warn("存在下级产品分类,不允许删除");
        }
        if (productsClassService.checkProductsClassExistProduct(productsClassId)) {
            return warn("产品分类存在产品,不允许删除");
        }
        productsClassService.checkProductsClassDataScope(productsClassId);
        return toAjax(productsClassService.deleteProductsClassById(productsClassId));
    }
}
