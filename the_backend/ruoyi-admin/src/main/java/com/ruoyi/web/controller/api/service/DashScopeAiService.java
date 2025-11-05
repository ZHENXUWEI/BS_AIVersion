package com.ruoyi.web.controller.api.service;

import com.ruoyi.web.controller.api.utils.DashScopeHttpUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class DashScopeAiService {
    private final DashScopeHttpUtils dashScopeHttpUtils;

    // 从配置文件读取API密钥
    public DashScopeAiService(@Value("${spring.ai.dashscope.api-key}") String apiKey) {
        this.dashScopeHttpUtils = new DashScopeHttpUtils(apiKey);
    }

    /**
     * 调用大模型处理政策分析
     */
    public String analyzePolicy(String question) {
        // 调用HTTP工具类，指定模型（如qwen-plus）
        return dashScopeHttpUtils.generate(question, "qwen-plus");
    }
}