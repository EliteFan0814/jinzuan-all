package com.ruoyi.web.controller.web;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.WebProduct;
import com.ruoyi.common.core.domain.entity.WebProductsClass;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.web.service.IWebProductService;
import com.ruoyi.web.service.IWebProductsClassService;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 产品信息
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/web/product")
public class WebProductController extends BaseController {
    @Autowired
    private IWebProductService webProductService;

    @Autowired
    private IWebProductsClassService productsClassService;

    /**
     * 获取产品列表
     */
    @PreAuthorize("@ss.hasPermi('web:product:list')")
    @GetMapping("/list")
    public TableDataInfo list(WebProduct webProduct) {
        startPage();
        List<WebProduct> list = webProductService.selectProductList(webProduct);
        return getDataTable(list);
    }

    @Log(title = "产品管理", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('web:product:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, WebProduct webProduct) {
        List<WebProduct> list = webProductService.selectProductList(webProduct);
        ExcelUtil<WebProduct> util = new ExcelUtil<WebProduct>(WebProduct.class);
        util.exportExcel(response, list, "产品数据");
    }

    @Log(title = "产品管理", businessType = BusinessType.IMPORT)
    @PreAuthorize("@ss.hasPermi('web:product:import')")
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<WebProduct> util = new ExcelUtil<WebProduct>(WebProduct.class);
        List<WebProduct> webProductList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = webProductService.importProduct(webProductList, updateSupport, operName);
        return success(message);
    }

    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response) {
        ExcelUtil<WebProduct> util = new ExcelUtil<WebProduct>(WebProduct.class);
        util.importTemplateExcel(response, "产品数据");
    }

    /**
     * 根据产品编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('web:product:query')")
    @GetMapping(value = {"/", "/{productId}"})
    public AjaxResult getInfo(@PathVariable(value = "productId", required = false) Long productId) {
        AjaxResult ajax = AjaxResult.success();
        if (StringUtils.isNotNull(productId)) {
            WebProduct webProduct = webProductService.selectProductById(productId);
            ajax.put(AjaxResult.DATA_TAG, webProduct);
        }
        return ajax;
    }

    /**
     * 新增产品
     */
    @PreAuthorize("@ss.hasPermi('web:product:add')")
    @Log(title = "产品管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody WebProduct webProduct) {
        if (!webProductService.checkProductNameUnique(webProduct)) {
            return error("新增产品'" + webProduct.getProductName() + "'失败，登录账号已存在");
        }
        webProduct.setCreateBy(getUsername());
        return toAjax(webProductService.insertProduct(webProduct));
    }

    /**
     * 修改产品
     */
    @PreAuthorize("@ss.hasPermi('web:product:edit')")
    @Log(title = "产品管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody WebProduct webProduct) {
        if (!webProductService.checkProductNameUnique(webProduct)) {
            return error("修改产品'" + webProduct.getProductName() + "'失败，登录账号已存在");
        }
        webProduct.setUpdateBy(getUsername());
        return toAjax(webProductService.updateProduct(webProduct));
    }

    /**
     * 删除产品
     */
    @PreAuthorize("@ss.hasPermi('web:product:remove')")
    @Log(title = "产品管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{productIds}")
    public AjaxResult remove(@PathVariable Long[] productIds) {
        if (ArrayUtils.contains(productIds, getUserId())) {
            return error("当前产品不能删除");
        }
        return toAjax(webProductService.deleteProductByIds(productIds));
    }


    /**
     * 状态修改
     */
    @PreAuthorize("@ss.hasPermi('web:product:edit')")
    @Log(title = "产品管理", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public AjaxResult changeStatus(@RequestBody WebProduct webProduct) {
        webProduct.setUpdateBy(getUsername());
        return toAjax(webProductService.updateProductStatus(webProduct));
    }


    /**
     * 获取部门树列表
     */
    @PreAuthorize("@ss.hasPermi('web:product:list')")
    @GetMapping("/productsClassTree")
    public AjaxResult productsClassTree(WebProductsClass productsClass) {
        return success(productsClassService.selectProductsClassTreeList(productsClass));
    }
}
