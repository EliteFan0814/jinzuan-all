package com.ruoyi.web.service.impl;

import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.domain.entity.WebProduct;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.bean.BeanValidators;
import com.ruoyi.web.mapper.WebProductMapper;
import com.ruoyi.web.service.IWebProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Validator;
import java.util.List;

/**
 * 产品 业务层处理
 *
 * @author ruoyi
 */
@Service
public class WebProductServiceImpl implements IWebProductService {
    private static final Logger log = LoggerFactory.getLogger(WebProductServiceImpl.class);

    @Autowired
    private WebProductMapper webProductMapper;

    @Autowired
    protected Validator validator;

    /**
     * 根据条件分页查询产品列表
     *
     * @param webProduct 产品信息
     * @return 产品信息集合信息
     */
    @Override
    // @DataScope(deptAlias = "d", userAlias = "u")
    public List<WebProduct> selectProductList(WebProduct webProduct) {
        return webProductMapper.selectProductList(webProduct);
    }

    /**
     * 通过产品名查询产品
     *
     * @param webProductName 产品名
     * @return 产品对象信息
     */
    @Override
    public WebProduct selectProductByProductName(String webProductName) {
        return webProductMapper.selectProductByProductName(webProductName);
    }

    /**
     * 通过产品英文名查询产品
     *
     * @param webProductNameEn 产品英文名
     * @return 产品对象信息
     */
    @Override
    public WebProduct selectProductByProductNameEn(String webProductNameEn) {
        return webProductMapper.selectProductByProductNameEn(webProductNameEn);
    }

    /**
     * 通过产品ID查询产品
     *
     * @param webProductId 产品ID
     * @return 产品对象信息
     */
    @Override
    public WebProduct selectProductById(Long webProductId) {
        return webProductMapper.selectProductById(webProductId);
    }

    /**
     * 校验产品名称是否唯一
     *
     * @param webProduct 产品信息
     * @return 结果
     */
    @Override
    public boolean checkProductNameUnique(WebProduct webProduct) {
        Long webProductId = StringUtils.isNull(webProduct.getProductId()) ? -1L : webProduct.getProductId();
        WebProduct info = webProductMapper.checkProductNameUnique(webProduct.getProductName());
        if (StringUtils.isNotNull(info) && info.getProductId().longValue() != webProductId.longValue()) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 新增保存产品信息
     *
     * @param webProduct 产品信息
     * @return 结果
     */
    @Override
    @Transactional
    public int insertProduct(WebProduct webProduct) {
        // 新增产品信息
        int rows = webProductMapper.insertProduct(webProduct);
        return rows;
    }

    /**
     * 注册产品信息
     *
     * @param webProduct 产品信息
     * @return 结果
     */
    @Override
    public boolean registerProduct(WebProduct webProduct) {
        return webProductMapper.insertProduct(webProduct) > 0;
    }

    /**
     * 修改保存产品信息
     *
     * @param webProduct 产品信息
     * @return 结果
     */
    @Override
    @Transactional
    public int updateProduct(WebProduct webProduct) {
        return webProductMapper.updateProduct(webProduct);
    }

    /**
     * 修改产品状态
     *
     * @param webProduct 产品信息
     * @return 结果
     */
    @Override
    public int updateProductStatus(WebProduct webProduct) {
        return webProductMapper.updateProduct(webProduct);
    }

    /**
     * 修改产品基本信息
     *
     * @param webProduct 产品信息
     * @return 结果
     */
    @Override
    public int updateProductProfile(WebProduct webProduct) {
        return webProductMapper.updateProduct(webProduct);
    }

    /**
     * 修改产品头像
     *
     * @param webProductName 产品名
     * @param avatar         头像地址
     * @return 结果
     */
    @Override
    public boolean updateProductAvatar(String webProductName, String avatar) {
        return webProductMapper.updateProductAvatar(webProductName, avatar) > 0;
    }

    /**
     * 通过产品ID删除产品
     *
     * @param webProductId 产品ID
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteProductById(Long webProductId) {
        return webProductMapper.deleteProductById(webProductId);
    }

    /**
     * 批量删除产品信息
     *
     * @param webProductIds 需要删除的产品ID
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteProductByIds(Long[] webProductIds) {
        return webProductMapper.deleteProductByIds(webProductIds);
    }

    /**
     * 导入产品数据
     *
     * @param webProductList  产品数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName        操作产品
     * @return 结果
     */
    @Override
    public String importProduct(List<WebProduct> webProductList, Boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(webProductList) || webProductList.size() == 0) {
            throw new ServiceException("导入产品数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        for (WebProduct webProduct : webProductList) {
            try {
                // 验证是否存在这个产品
                WebProduct u = webProductMapper.selectProductByProductName(webProduct.getProductName());
                if (StringUtils.isNull(u)) {
                    BeanValidators.validateWithException(validator, webProduct);
                    webProduct.setCreateBy(operName);
                    webProductMapper.insertProduct(webProduct);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、账号 " + webProduct.getProductName() + " 导入成功");
                } else if (isUpdateSupport) {
                    BeanValidators.validateWithException(validator, webProduct);
                    webProduct.setProductId(u.getProductId());
                    webProduct.setUpdateBy(operName);
                    webProductMapper.updateProduct(webProduct);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、账号 " + webProduct.getProductName() + " 更新成功");
                } else {
                    failureNum++;
                    failureMsg.append("<br/>" + failureNum + "、账号 " + webProduct.getProductName() + " 已存在");
                }
            } catch (Exception e) {
                failureNum++;
                String msg = "<br/>" + failureNum + "、账号 " + webProduct.getProductName() + " 导入失败：";
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
