package com.ruoyi.common.core.domain.entity;

import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

/**
 * 产品分类表 web_products_class
 *
 * @author fanpeichao
 */
public class WebProductsClass extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 产品类ID
     */
    private Long productClassId;

    /**
     * 产品父类ID
     */
    private Long parentId;

    /**
     * 祖级列表
     */
    private String ancestors;

    /**
     * 产品类名称
     */
    private String productClassName;

    /**
     * 产品类英文名称
     */
    private String productClassNameEn;

    /**
     * 产品类图片
     */
    private String avatar;

    /**
     * 显示顺序
     */
    private Integer orderNum;

    /**
     * 负责人
     */
    private String leader;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 产品分类状态:0正常,1停用
     */
    private String status;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    private String delFlag;

    /**
     * 父类名称
     */
    private String parentName;

    /**
     * 子类
     */
    private List<WebProductsClass> children = new ArrayList<WebProductsClass>();

    public Long getProductClassId() {
        return productClassId;
    }

    public void setProductClassId(Long productClassId) {
        this.productClassId = productClassId;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getAncestors() {
        return ancestors;
    }

    public void setAncestors(String ancestors) {
        this.ancestors = ancestors;
    }

    @NotBlank(message = "产品类别不能为空")
    @Size(min = 0, max = 200, message = "产品类别长度不能超过200个字符")
    public String getProductClassName() {
        return productClassName;
    }

    public void setProductClassName(String productClassName) {
        this.productClassName = productClassName;
    }

    @NotBlank(message = "产品英文类别不能为空")
    @Size(min = 0, max = 200, message = "产品英文类别长度不能超过200个字符")
    public String getProductClassNameEn() {
        return productClassNameEn;
    }

    public void setProductClassNameEn(String productClassNameEn) {
        this.productClassNameEn = productClassNameEn;
    }

    @NotBlank(message = "产品类别图片不能为空")
    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @NotNull(message = "显示顺序不能为空")
    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    public String getLeader() {
        return leader;
    }

    public void setLeader(String leader) {
        this.leader = leader;
    }

    @Size(min = 0, max = 11, message = "联系电话长度不能超过11个字符")
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Email(message = "邮箱格式不正确")
    @Size(min = 0, max = 50, message = "邮箱长度不能超过50个字符")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public List<WebProductsClass> getChildren() {
        return children;
    }

    public void setChildren(List<WebProductsClass> children) {
        this.children = children;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("productClassId", getProductClassId())
                .append("parentId", getParentId())
                .append("ancestors", getAncestors())
                .append("productClassName", getProductClassName())
                .append("productClassNameEn", getProductClassNameEn())
                .append("avatar", getAvatar())
                .append("orderNum", getOrderNum())
                .append("leader", getLeader())
                .append("phone", getPhone())
                .append("email", getEmail())
                .append("status", getStatus())
                .append("delFlag", getDelFlag())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
