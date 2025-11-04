package com.ruoyi.generator.service;

import com.ruoyi.generator.domain.dto.AiChatRequest;
import com.ruoyi.generator.domain.dto.AiResponse;

/**
 * AI模型服务接口
 * filePath: BS_AIVersion/the_backend/ruoyi-generator/src/main/java/com/ruoyi/generator/service/IAIService.java
 */
public interface IAIService {

    /**
     * 调用AI模型生成内容
     * @param prompt 提示词
     * @return 生成结果
     */
    String generate(String prompt);
    AiResponse handleChat(AiChatRequest request);
}