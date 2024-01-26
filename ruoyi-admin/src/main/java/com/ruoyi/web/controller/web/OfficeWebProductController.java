package com.ruoyi.web.controller.web;

import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.WebProduct;
import com.ruoyi.common.core.domain.entity.WebProductsClass;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.web.service.IWebProductService;
import com.ruoyi.web.service.IWebProductsClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 产品信息
 *
 * @author ruoyi
 */
@Anonymous
@RestController
@RequestMapping("/webOffice/product")
public class OfficeWebProductController extends BaseController {
    @Autowired
    private IWebProductService webProductService;

    @Autowired
    private IWebProductsClassService productsClassService;

    /**
     * 获取产品列表
     */
    @GetMapping("/list")
    public TableDataInfo list(WebProduct webProduct) {
        startPage();
        List<WebProduct> list = webProductService.selectProductList(webProduct);
        return getDataTable(list);
    }

    /**
     * 根据产品编号获取详细信息
     */
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
     * 获取部门树列表
     */
    @GetMapping("/productsClassTree")
    public AjaxResult productsClassTree(WebProductsClass productsClass) {
        return success(productsClassService.selectProductsClassTreeList(productsClass));
    }
}
