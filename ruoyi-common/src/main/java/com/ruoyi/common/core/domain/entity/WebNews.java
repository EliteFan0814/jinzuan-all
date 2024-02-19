package com.ruoyi.common.core.domain.entity;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.annotation.Excel.ColumnType;
import com.ruoyi.common.core.domain.BaseEntity;
import com.ruoyi.common.xss.Xss;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 新闻对象 web_news
 *
 * @author ruoyi
 */
public class WebNews extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 新闻ID
     */
    @Excel(name = "新闻ID", cellType = ColumnType.NUMERIC, prompt = "新闻ID")
    private Long newsId;

    /**
     * 新闻名称
     */
    @Excel(name = "新闻名称")
    private String newsName;
    /**
     * 新闻头像
     */
    private String avatar;


    /**
     * 新闻状态（0正常 1停用）
     */
    @Excel(name = "新闻状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    private String delFlag;
    /**
     * 新闻内容
     */
    private String content;

    public WebNews() {

    }

    public WebNews(Long newsId) {
        this.newsId = newsId;
    }

    public Long getNewsId() {
        return newsId;
    }

    public void setNewsId(Long newsId) {
        this.newsId = newsId;
    }

    @Xss(message = "新闻名称不能包含脚本字符")
    @NotBlank(message = "新闻名称不能为空")
    @Size(min = 0, max = 500, message = "新闻名称长度不能超过500个字符")
    public String getNewsName() {
        return newsName;
    }

    public void setNewsName(String newsName) {
        this.newsName = newsName;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("newsId", getNewsId())
                .append("newsName", getNewsName())
                .append("avatar", getAvatar())
                .append("status", getStatus())
                .append("delFlag", getDelFlag())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark()).toString();
    }
}
