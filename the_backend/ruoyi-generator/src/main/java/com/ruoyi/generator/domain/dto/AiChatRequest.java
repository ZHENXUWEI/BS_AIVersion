package com.ruoyi.generator.domain.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * AI聊天请求DTO
 * filePath: BS_AIVersion/the_backend/ruoyi-generator/src/main/java/com/ruoyi/generator/domain/dto/AiChatRequest.java
 */
@Data
public class AiChatRequest {
    @NotBlank(message = "问题不能为空")
    private String question;

    /** 可选：指定知识库分类 */
    private String category;
}