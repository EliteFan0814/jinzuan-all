package com.ruoyi.common.core.domain.entity;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.annotation.Excel.ColumnType;
import com.ruoyi.common.annotation.Excel.Type;
import com.ruoyi.common.annotation.Excels;
import com.ruoyi.common.core.domain.BaseEntity;
import com.ruoyi.common.xss.Xss;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 产品对象 web_product
 *
 * @author ruoyi
 */
public class WebProduct extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 产品ID
     */
    @Excel(name = "产品序号", cellType = ColumnType.NUMERIC, prompt = "产品编号")
    private Long productId;

    /**
     * 产品类ID
     */
    @Excel(name = "产品类编号", type = Type.IMPORT)
    private Long classId;

    /**
     * 产品名称
     */
    @Excel(name = "产品名称")
    private String productName;

    /**
     * 产品英文名称
     */
    @Excel(name = "产品英文名称")
    private String productNameEn;

    /**
     * 产品头像
     */
    private String avatar;


    /**
     * 产品状态（0正常 1停用）
     */
    @Excel(name = "产品状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    private String delFlag;

    /**
     * 产品类对象
     */
    @Excels({@Excel(name = "产品类名称", targetAttr = "productClassName", type = Type.EXPORT), @Excel(name = "产品类负责人",
            targetAttr
                    = "leader", type = Type.EXPORT)})
    private WebProductsClass productClass;

    public WebProduct() {

    }

    public WebProduct(Long productId) {
        this.productId = productId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getClassId() {
        return classId;
    }

    public void setClassId(Long classId) {
        this.classId = classId;
    }

    @Xss(message = "产品英文名称不能包含脚本字符")
    @Size(min = 0, max = 30, message = "产品英文名称长度不能超过30个字符")
    public String getProductNameEn() {
        return productNameEn;
    }

    public void setProductNameEn(String productNameEn) {
        this.productNameEn = productNameEn;
    }

    @Xss(message = "产品名称不能包含脚本字符")
    @NotBlank(message = "产品名称不能为空")
    @Size(min = 0, max = 30, message = "产品名称长度不能超过30个字符")
    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }


    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }


    public WebProductsClass getProductClass() {
        return productClass;
    }

    public void setProductClass(WebProductsClass productClass) {
        this.productClass = productClass;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("productId", getProductId())
                .append("classId", getClassId())
                .append("productName", getProductName())
                .append("productNameEn", getProductNameEn())
                .append("avatar", getAvatar())
                .append("status", getStatus())
                .append("delFlag", getDelFlag())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .append("productClass", getProductClass()).toString();
    }
}
