package com.ruoyi.web.controller.policy.domain.dto;

import com.ruoyi.common.annotation.Excel;

public class FutureModelExportDto {
    @Excel(name = "企业名称", width = 30)
    private String companyName;

    @Excel(name = "今年预测已达标", width = 20)
    private String reach;

    @Excel(name = "即将达标", width = 20)
    private String almostReach;

    @Excel(name = "未达标", width = 20)
    private String notReach;

    @Excel(name = "指标情况", width = 50)
    private String indicatorDetail;

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getReach() {
        return reach;
    }

    public void setReach(String reach) {
        this.reach = reach;
    }

    public String getAlmostReach() {
        return almostReach;
    }

    public void setAlmostReach(String almostReach) {
        this.almostReach = almostReach;
    }

    public String getNotReach() {
        return notReach;
    }

    public void setNotReach(String notReach) {
        this.notReach = notReach;
    }

    public String getIndicatorDetail() {
        return indicatorDetail;
    }

    public void setIndicatorDetail(String indicatorDetail) {
        this.indicatorDetail = indicatorDetail;
    }
} 