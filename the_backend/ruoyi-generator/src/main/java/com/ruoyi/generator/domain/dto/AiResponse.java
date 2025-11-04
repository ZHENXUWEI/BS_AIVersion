package com.ruoyi.generator.domain.dto;

import lombok.Data;

/**
 * AI响应结果DTO
 * filePath: BS_AIVersion/the_backend/ruoyi-generator/src/main/java/com/ruoyi/generator/domain/dto/AiResponse.java
 */
@Data
public class AiResponse {
    private String content;
    private Long knowledgeId; // 关联的主要知识库ID（可选）
}