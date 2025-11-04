package com.ruoyi.web.controller.policy.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class PolicyCategoryData extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Integer id;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Integer policyCategoryId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String content;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Integer status;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String type;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Integer belong;

    public PolicyCategoryData(Integer id, Integer policyCategoryId, String content, Integer status) {
        this.id = id;
        this.policyCategoryId = policyCategoryId;
        this.content = content;
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getBelong() {
        return belong;
    }

    public void setBelong(Integer belong) {
        this.belong = belong;
    }

    public PolicyCategoryData() {
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Integer getId()
    {
        return id;
    }
    public void setPolicyCategoryId(Integer policyCategoryId)
    {
        this.policyCategoryId = policyCategoryId;
    }

    public Integer getPolicyCategoryId()
    {
        return policyCategoryId;
    }
    public void setContent(String content)
    {
        this.content = content;
    }

    public String getContent()
    {
        return content;
    }
    public void setStatus(Integer status)
    {
        this.status = status;
    }

    public Integer getStatus()
    {
        return status;
    }

    public PolicyCategoryData(Integer id, Integer policyCategoryId, String content, Integer status, String type, Integer belong) {
        this.id = id;
        this.policyCategoryId = policyCategoryId;
        this.content = content;
        this.status = status;
        this.type = type;
        this.belong = belong;
    }

    @Override
    public String toString() {
        return "PolicyCategoryData{" +
                "id=" + id +
                ", policyCategoryId=" + policyCategoryId +
                ", content='" + content + '\'' +
                ", status=" + status +
                ", type='" + type + '\'' +
                ", belong=" + belong +
                '}';
    }
}