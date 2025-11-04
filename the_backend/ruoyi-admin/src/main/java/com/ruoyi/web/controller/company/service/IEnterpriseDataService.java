package com.ruoyi.web.controller.company.service;

import com.ruoyi.web.controller.api.controller.EnterpriseController.IndicatorResult;
import com.ruoyi.web.controller.api.common.Result;
import com.ruoyi.web.controller.api.domain.dto.CompanyIndicatorRecordMap;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

public interface IEnterpriseDataService {
    /**
     * 处理企业数据更新
     * @param indicatorResults 指标结果列表
     * @return 处理结果
     */
    Result processEnterpriseData(List<IndicatorResult> indicatorResults)
            throws IntrospectionException, InvocationTargetException, IllegalAccessException, NoSuchMethodException;

    /**
     * 获取企业数据
     * @param companyId 企业ID
     * @param year 年份
     * @return 企业指标数据列表
     */
    List<CompanyIndicatorRecordMap> getEnterpriseData(Integer companyId, Integer year)
            throws InvocationTargetException, IllegalAccessException, NoSuchMethodException;
} 