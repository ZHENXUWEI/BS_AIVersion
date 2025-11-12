// BS_AIVersion/the_backend/ruoyi-admin/src/main/java/com/ruoyi/web/controller/api/controller/ModelMatchController.java
package com.ruoyi.web.controller.api.controller;

import com.ruoyi.web.controller.api.common.Result;
import com.ruoyi.web.controller.api.domain.IndicatorDetail;
import com.ruoyi.web.controller.api.domain.MatchResult;
import com.ruoyi.web.controller.policy.service.IPolicyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 模型匹配控制器
 */
@RestController
@RequestMapping("/api/model")
public class ModelMatchController {

    @Autowired
    private IPolicyService policyService;

    /**
     * 获取模型所需指标
     */
    @PostMapping("/indicators")
    public Result<List<IndicatorDetail>> getModelIndicators(@RequestBody Map<String, Object> params) {
        Integer modelId = Integer.parseInt(params.get("modelId").toString());
        // 从数据库获取指定模型的指标信息
        List<IndicatorDetail> indicators = policyService.getIndicatorsByModelId(modelId);
        return Result.success(indicators);
    }

    /**
     * 模型匹配处理
     */
    @PostMapping("/match")
    public Result<MatchResult> matchModelData(@RequestBody Map<String, Object> params) {
        Integer modelId = Integer.parseInt(params.get("modelId").toString());
        Map<String, Object> inputData = (Map<String, Object>) params.get("data");

        // 获取模型的指标阈值信息
        List<IndicatorDetail> indicators = policyService.getIndicatorsByModelId(modelId);
        List<IndicatorDetail> details = new ArrayList<>();
        boolean isSuccess = true;

        // 逐一比对每个指标
        for (IndicatorDetail indicator : indicators) {
            IndicatorDetail detail = new IndicatorDetail();
            detail.setName(indicator.getName());
            detail.setKey(indicator.getKey());
            detail.setThreshold(indicator.getThreshold());

            // 获取用户输入的值
            Object inputValue = inputData.get(indicator.getKey());
            detail.setValue(inputValue != null ? inputValue.toString() : "");

            // 检查是否达标
            boolean status = checkIndicator(indicator, inputValue);
            detail.setStatus(status);

            if (!status) {
                isSuccess = false;
            }

            details.add(detail);
        }

        MatchResult result = new MatchResult();
        result.setSuccess(isSuccess);
        result.setDetails(details);

        return Result.success(result);
    }

    /**
     * 检查单个指标是否达标
     */
    private boolean checkIndicator(IndicatorDetail indicator, Object inputValue) {
        if (inputValue == null || inputValue.toString().isEmpty()) {
            return false;
        }

        String threshold = indicator.getThreshold();
        String type = indicator.getType();

        try {
            // 根据指标类型进行不同的比较逻辑
            switch (type) {
                case "number":
                    double value = Double.parseDouble(inputValue.toString());
                    // 阈值格式可能为 ">=100" 或 "100-200" 等
                    return compareNumber(value, threshold);
                case "text":
                    return compareText(inputValue.toString(), threshold);
                case "date":
                    return compareDate(inputValue.toString(), threshold);
                default:
                    return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    // 数字比较逻辑
    private boolean compareNumber(double value, String threshold) {
        // 实现数字比较逻辑，支持 >, <, >=, <=, = 以及范围比较
        // 例如 threshold 可能是 ">=100" 或 "100-200"
        if (threshold.startsWith(">=")) {
            double t = Double.parseDouble(threshold.substring(2));
            return value >= t;
        } else if (threshold.startsWith("<=")) {
            double t = Double.parseDouble(threshold.substring(2));
            return value <= t;
        } else if (threshold.startsWith(">")) {
            double t = Double.parseDouble(threshold.substring(1));
            return value > t;
        } else if (threshold.startsWith("<")) {
            double t = Double.parseDouble(threshold.substring(1));
            return value < t;
        } else if (threshold.startsWith("=")) {
            double t = Double.parseDouble(threshold.substring(1));
            return value == t;
        } else if (threshold.contains("-")) {
            String[] parts = threshold.split("-");
            double min = Double.parseDouble(parts[0]);
            double max = Double.parseDouble(parts[1]);
            return value >= min && value <= max;
        }
        return false;
    }

    // 文本比较逻辑
    private boolean compareText(String value, String threshold) {
        // 实现文本比较逻辑，如包含特定字符、等于特定值等
        return threshold.contains(value) || value.equals(threshold);
    }

    // 日期比较逻辑
    private boolean compareDate(String value, String threshold) {
        // 实现日期比较逻辑
        // 简化实现，实际应使用日期工具类
        return value.compareTo(threshold) >= 0;
    }
}