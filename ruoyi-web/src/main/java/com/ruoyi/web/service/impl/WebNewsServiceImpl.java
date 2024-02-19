package com.ruoyi.web.service.impl;

import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.domain.entity.WebNews;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.bean.BeanValidators;
import com.ruoyi.web.mapper.WebNewsMapper;
import com.ruoyi.web.service.IWebNewsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Validator;
import java.util.List;

/**
 * 新闻 业务层处理
 *
 * @author ruoyi
 */
@Service
public class WebNewsServiceImpl implements IWebNewsService {
    private static final Logger log = LoggerFactory.getLogger(WebNewsServiceImpl.class);

    @Autowired
    private WebNewsMapper webNewsMapper;

    @Autowired
    protected Validator validator;

    /**
     * 根据条件分页查询新闻列表
     *
     * @param webNews 新闻信息
     * @return 新闻信息集合信息
     */
    @Override
    // @DataScope(deptAlias = "d", userAlias = "u")
    public List<WebNews> selectNewsList(WebNews webNews) {
        return webNewsMapper.selectNewsList(webNews);
    }

    /**
     * 通过新闻名查询新闻
     *
     * @param webNewsName 新闻名
     * @return 新闻对象信息
     */
    @Override
    public WebNews selectNewsByNewsName(String webNewsName) {
        return webNewsMapper.selectNewsByNewsName(webNewsName);
    }

    /**
     * 通过新闻英文名查询新闻
     *
     * @param webNewsNameEn 新闻英文名
     * @return 新闻对象信息
     */
    @Override
    public WebNews selectNewsByNewsNameEn(String webNewsNameEn) {
        return webNewsMapper.selectNewsByNewsNameEn(webNewsNameEn);
    }

    /**
     * 通过新闻ID查询新闻
     *
     * @param webNewsId 新闻ID
     * @return 新闻对象信息
     */
    @Override
    public WebNews selectNewsById(Long webNewsId) {
        return webNewsMapper.selectNewsById(webNewsId);
    }

    /**
     * 校验新闻名称是否唯一
     *
     * @param webNews 新闻信息
     * @return 结果
     */
    @Override
    public boolean checkNewsNameUnique(WebNews webNews) {
        Long webNewsId = StringUtils.isNull(webNews.getNewsId()) ? -1L : webNews.getNewsId();
        WebNews info = webNewsMapper.checkNewsNameUnique(webNews.getNewsName());
        if (StringUtils.isNotNull(info) && info.getNewsId().longValue() != webNewsId.longValue()) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 新增保存新闻信息
     *
     * @param webNews 新闻信息
     * @return 结果
     */
    @Override
    @Transactional
    public int insertNews(WebNews webNews) {
        // 新增新闻信息
        int rows = webNewsMapper.insertNews(webNews);
        return rows;
    }

    /**
     * 注册新闻信息
     *
     * @param webNews 新闻信息
     * @return 结果
     */
    @Override
    public boolean registerNews(WebNews webNews) {
        return webNewsMapper.insertNews(webNews) > 0;
    }

    /**
     * 修改保存新闻信息
     *
     * @param webNews 新闻信息
     * @return 结果
     */
    @Override
    @Transactional
    public int updateNews(WebNews webNews) {
        return webNewsMapper.updateNews(webNews);
    }

    /**
     * 修改新闻状态
     *
     * @param webNews 新闻信息
     * @return 结果
     */
    @Override
    public int updateNewsStatus(WebNews webNews) {
        return webNewsMapper.updateNews(webNews);
    }

    /**
     * 修改新闻基本信息
     *
     * @param webNews 新闻信息
     * @return 结果
     */
    @Override
    public int updateNewsProfile(WebNews webNews) {
        return webNewsMapper.updateNews(webNews);
    }

    /**
     * 修改新闻头像
     *
     * @param webNewsName 新闻名
     * @param avatar         头像地址
     * @return 结果
     */
    @Override
    public boolean updateNewsAvatar(String webNewsName, String avatar) {
        return webNewsMapper.updateNewsAvatar(webNewsName, avatar) > 0;
    }

    /**
     * 通过新闻ID删除新闻
     *
     * @param webNewsId 新闻ID
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteNewsById(Long webNewsId) {
        return webNewsMapper.deleteNewsById(webNewsId);
    }

    /**
     * 批量删除新闻信息
     *
     * @param webNewsIds 需要删除的新闻ID
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteNewsByIds(Long[] webNewsIds) {
        return webNewsMapper.deleteNewsByIds(webNewsIds);
    }

    /**
     * 导入新闻数据
     *
     * @param webNewsList  新闻数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName        操作新闻
     * @return 结果
     */
    @Override
    public String importNews(List<WebNews> webNewsList, Boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(webNewsList) || webNewsList.size() == 0) {
            throw new ServiceException("导入新闻数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        for (WebNews webNews : webNewsList) {
            try {
                // 验证是否存在这个新闻
                WebNews u = webNewsMapper.selectNewsByNewsName(webNews.getNewsName());
                if (StringUtils.isNull(u)) {
                    BeanValidators.validateWithException(validator, webNews);
                    webNews.setCreateBy(operName);
                    webNewsMapper.insertNews(webNews);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、账号 " + webNews.getNewsName() + " 导入成功");
                } else if (isUpdateSupport) {
                    BeanValidators.validateWithException(validator, webNews);
                    webNews.setNewsId(u.getNewsId());
                    webNews.setUpdateBy(operName);
                    webNewsMapper.updateNews(webNews);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、账号 " + webNews.getNewsName() + " 更新成功");
                } else {
                    failureNum++;
                    failureMsg.append("<br/>" + failureNum + "、账号 " + webNews.getNewsName() + " 已存在");
                }
            } catch (Exception e) {
                failureNum++;
                String msg = "<br/>" + failureNum + "、账号 " + webNews.getNewsName() + " 导入失败：";
                failureMsg.append(msg + e.getMessage());
                log.error(msg, e);
            }
        }
        if (failureNum > 0) {
            failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
            throw new ServiceException(failureMsg.toString());
        } else {
            successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
        }
        return successMsg.toString();
    }
}
