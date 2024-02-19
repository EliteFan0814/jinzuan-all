package com.ruoyi.web.controller.web;

import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.WebNews;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.web.service.IWebNewsService;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 新闻信息
 *
 * @author ruoyi
 */
@Anonymous
@RestController
@RequestMapping("/webOffice/news")
public class OfficeWebNewsController extends BaseController {
    @Autowired
    private IWebNewsService webNewsService;

    /**
     * 获取新闻列表
     */
    @GetMapping("/list")
    public TableDataInfo list(WebNews webNews) {
        startPage();
        List<WebNews> list = webNewsService.selectNewsList(webNews);
        return getDataTable(list);
    }

    /**
     * 根据新闻编号获取详细信息
     */
    @GetMapping(value = {"/", "/{newsId}"})
    public AjaxResult getInfo(@PathVariable(value = "newsId", required = false) Long newsId) {
        AjaxResult ajax = AjaxResult.success();
        if (StringUtils.isNotNull(newsId)) {
            WebNews webNews = webNewsService.selectNewsById(newsId);
            ajax.put(AjaxResult.DATA_TAG, webNews);
        }
        return ajax;
    }
}
