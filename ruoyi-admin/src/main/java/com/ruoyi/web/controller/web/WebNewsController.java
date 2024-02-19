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
@RestController
@RequestMapping("/web/news")
public class WebNewsController extends BaseController {
    @Autowired
    private IWebNewsService webNewsService;

    /**
     * 获取新闻列表
     */
    @PreAuthorize("@ss.hasPermi('web:news:list')")
    @GetMapping("/list")
    public TableDataInfo list(WebNews webNews) {
        startPage();
        List<WebNews> list = webNewsService.selectNewsList(webNews);
        return getDataTable(list);
    }

    @Log(title = "新闻管理", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('web:news:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, WebNews webNews) {
        List<WebNews> list = webNewsService.selectNewsList(webNews);
        ExcelUtil<WebNews> util = new ExcelUtil<WebNews>(WebNews.class);
        util.exportExcel(response, list, "新闻数据");
    }

    @Log(title = "新闻管理", businessType = BusinessType.IMPORT)
    @PreAuthorize("@ss.hasPermi('web:news:import')")
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<WebNews> util = new ExcelUtil<WebNews>(WebNews.class);
        List<WebNews> webNewsList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = webNewsService.importNews(webNewsList, updateSupport, operName);
        return success(message);
    }

    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response) {
        ExcelUtil<WebNews> util = new ExcelUtil<WebNews>(WebNews.class);
        util.importTemplateExcel(response, "新闻数据");
    }

    /**
     * 根据新闻编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('web:news:query')")
    @GetMapping(value = {"/", "/{newsId}"})
    public AjaxResult getInfo(@PathVariable(value = "newsId", required = false) Long newsId) {
        AjaxResult ajax = AjaxResult.success();
        if (StringUtils.isNotNull(newsId)) {
            WebNews webNews = webNewsService.selectNewsById(newsId);
            ajax.put(AjaxResult.DATA_TAG, webNews);
        }
        return ajax;
    }

    /**
     * 新增新闻
     */
    @PreAuthorize("@ss.hasPermi('web:news:add')")
    @Log(title = "新闻管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody WebNews webNews) {
        if (!webNewsService.checkNewsNameUnique(webNews)) {
            return error("新增新闻'" + webNews.getNewsName() + "'失败，新闻名称已存在");
        }
        webNews.setCreateBy(getUsername());
        return toAjax(webNewsService.insertNews(webNews));
    }

    /**
     * 修改新闻
     */
    @PreAuthorize("@ss.hasPermi('web:news:edit')")
    @Log(title = "新闻管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody WebNews webNews) {
        if (!webNewsService.checkNewsNameUnique(webNews)) {
            return error("修改新闻'" + webNews.getNewsName() + "'失败，登录账号已存在");
        }
        webNews.setUpdateBy(getUsername());
        return toAjax(webNewsService.updateNews(webNews));
    }

    /**
     * 删除新闻
     */
    @PreAuthorize("@ss.hasPermi('web:news:remove')")
    @Log(title = "新闻管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{newsIds}")
    public AjaxResult remove(@PathVariable Long[] newsIds) {
        if (ArrayUtils.contains(newsIds, getUserId())) {
            return error("当前新闻不能删除");
        }
        return toAjax(webNewsService.deleteNewsByIds(newsIds));
    }


    /**
     * 状态修改
     */
    @PreAuthorize("@ss.hasPermi('web:news:edit')")
    @Log(title = "新闻管理", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public AjaxResult changeStatus(@RequestBody WebNews webNews) {
        webNews.setUpdateBy(getUsername());
        return toAjax(webNewsService.updateNewsStatus(webNews));
    }
}
