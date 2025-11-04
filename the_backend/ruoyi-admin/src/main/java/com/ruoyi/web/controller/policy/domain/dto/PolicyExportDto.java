package com.ruoyi.web.controller.policy.domain.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.web.controller.policy.domain.Policy;

/**
 * 政策模型导出数据对象
 */
public class PolicyExportDto{
    private static final long serialVersionUID = 1L;

    /** 政策名称 */
    @Excel(name = "政策模型名称", width = 30)
    private String name;

    /** 存量企业 */
    @Excel(name = "存量企业", width = 30)
    private String stockCompany;

    /** 增量企业 */
    @Excel(name = "增量企业", width = 30)
    private String cultivateCompany;

    /** 退步企业 */
    @Excel(name = "退步企业", width = 30)
    private String retreatCompany;

    /** 匹配公式 */
    @Excel(name = "匹配公式", width = 30)
    private String formula;

    /** 政策条件 */
    @Excel(name = "政策指标要求和企业指标详情", width = 50)
    private String policyCondition;

    /** 使用状态 */
    @Excel(name = "使用状态", readConverterExp = "0=使用中,1=停用中")
    private Integer status;

    public PolicyExportDto() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStockCompany() {
        return stockCompany;
    }

    public void setStockCompany(String stockCompany) {
        this.stockCompany = stockCompany;
    }

    public String getCultivateCompany() {
        return cultivateCompany;
    }

    public void setCultivateCompany(String cultivateCompany) {
        this.cultivateCompany = cultivateCompany;
    }

    public String getRetreatCompany() {
        return retreatCompany;
    }

    public void setRetreatCompany(String retreatCompany) {
        this.retreatCompany = retreatCompany;
    }


    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    public String getPolicyCondition() {
        return policyCondition;
    }

    public void setPolicyCondition(String policyCondition) {
        this.policyCondition = policyCondition;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 将Policy对象转换为导出DTO
     */
    public static PolicyExportDto fromPolicy(Policy policy) {
        PolicyExportDto dto = new PolicyExportDto();
        dto.setName(policy.getName());
        dto.setFormula(policy.getFormula());
        dto.setPolicyCondition(policy.getPolicyCondition());
        dto.setStatus(policy.getStatus());
        
        // TODO: 这里需要根据实际业务逻辑来设置各类企业的状态
        // 例如：从其他服务或数据中获取企业分类信息
        // dto.setStockCompany("去年达标，今年达标");
        // dto.setCultivateCompany("去年未达标，今年达标");
        // dto.setRetreatCompany("去年达标，今年未达标");
        // dto.setIncrementCompany("新增企业");
        
        return dto;
    }
} 