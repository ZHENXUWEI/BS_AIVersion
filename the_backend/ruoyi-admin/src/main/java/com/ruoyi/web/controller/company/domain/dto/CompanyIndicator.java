package com.ruoyi.web.controller.company.domain.dto;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

public class CompanyIndicator extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private Integer id;

    @Excel(name = "dictionaryId")
    private Integer dictionaryId;

    @Excel(name = "companyId")
    private Integer companyId;

    @Excel(name = "指标名称")
    private String name;

    @Excel(name = "指标英译")
    private String key;

    @Excel(name = "数字阈值")
    private Double threshold;

    @Excel(name = "文字阈值")
    private String remark;

    @Excel(name = "指标类型")
    private String type;

    @Excel(name = "年份")
    private Integer year;

    public CompanyIndicator(Integer id, Integer dictionaryId, Integer companyId, String name, String key, Double threshold) {
        this.id = id;
        this.dictionaryId = dictionaryId;
        this.companyId = companyId;
        this.name = name;
        this.key = key;
        this.threshold = threshold;
    }

    public CompanyIndicator(Integer id, Integer dictionaryId, Integer companyId, String remark, Integer year) {
        this.id = id;
        this.dictionaryId = dictionaryId;
        this.companyId = companyId;
        this.remark = remark;
        this.year = year;
    }

    public CompanyIndicator(Integer id, Integer dictionaryId, Integer companyId, Double threshold, Integer year) {
        this.id = id;
        this.dictionaryId = dictionaryId;
        this.companyId = companyId;
        this.threshold = threshold;
        this.year = year;
    }


    @Override
    public String toString() {
        return "CompanyIndicator{" +
                "id=" + id +
                ", dictionaryId=" + dictionaryId +
                ", companyId=" + companyId +
                ", name='" + name + '\'' +
                ", key='" + key + '\'' +
                ", threshold=" + threshold +
                ", remark='" + remark + '\'' +
                ", type='" + type + '\'' +
                ", year='" + year + '\'' +
                '}';
    }

    public CompanyIndicator(Integer id, Integer dictionaryId, Integer companyId, String name, String key, Double threshold, String remark, String type, Integer year) {
        this.id = id;
        this.dictionaryId = dictionaryId;
        this.companyId = companyId;
        this.name = name;
        this.key = key;
        this.threshold = threshold;
        this.remark = remark;
        this.type = type;
        this.year = year;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    @Override
    public String getRemark() {
        return remark;
    }

    @Override
    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public CompanyIndicator(Integer id, Integer dictionaryId, Integer companyId, String name, String key, Double threshold, String remark, String type) {
        this.id = id;
        this.dictionaryId = dictionaryId;
        this.companyId = companyId;
        this.name = name;
        this.key = key;
        this.threshold = threshold;
        this.remark = remark;
        this.type = type;
    }

    public CompanyIndicator() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDictionaryId() {
        return dictionaryId;
    }

    public void setDictionaryId(Integer dictionaryId) {
        this.dictionaryId = dictionaryId;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Double getThreshold() {
        return threshold;
    }

    public void setThreshold(Double threshold) {
        this.threshold = threshold;
    }



}
