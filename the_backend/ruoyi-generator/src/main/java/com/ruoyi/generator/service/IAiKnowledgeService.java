package com.ruoyi.generator.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.generator.domain.AiKnowledge;
import com.ruoyi.generator.domain.dto.AiChatRequest;
import com.ruoyi.generator.domain.dto.AiResponse;

import java.util.List;

/**
 * 知识库服务接口
 * filePath: BS_AIVersion/the_backend/ruoyi-generator/src/main/java/com/ruoyi/generator/service/IAiKnowledgeService.java
 */
public interface IAiKnowledgeService extends IService<AiKnowledge> {

    /**
     * 新增知识库文档（自动生成向量）
     */
    void addKnowledge(AiKnowledge knowledge);

    /**
     * 基于知识库的问答
     */
    AiResponse knowledgeChat(AiChatRequest request);

    /**
     * 根据分类查询知识库
     */
    List<AiKnowledge> listByCategory(String category);

    /**
     * 删除知识库文档（级联删除向量）
     */
    boolean removeKnowledge(Long id);
}