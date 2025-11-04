package com.ruoyi.generator.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.generator.domain.AiKnowledge;
import com.ruoyi.generator.domain.AiKnowledgeVector;
import com.ruoyi.generator.domain.dto.AiChatRequest;
import com.ruoyi.generator.domain.dto.AiResponse;
import com.ruoyi.generator.mapper.AiKnowledgeMapper;
import com.ruoyi.generator.mapper.AiKnowledgeVectorMapper;
import com.ruoyi.generator.service.IAiKnowledgeService;
import com.ruoyi.generator.service.IEmbeddingService;
import com.ruoyi.generator.service.IAIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 知识库服务实现
 * filePath: BS_AIVersion/the_backend/ruoyi-generator/src/main/java/com/ruoyi/generator/service/impl/AiKnowledgeServiceImpl.java
 */
@Service
public class AiKnowledgeServiceImpl extends ServiceImpl<AiKnowledgeMapper, AiKnowledge> implements IAiKnowledgeService {

    @Autowired
    private AiKnowledgeMapper knowledgeMapper;

    @Autowired
    private AiKnowledgeVectorMapper vectorMapper;

    @Autowired
    private IEmbeddingService embeddingService;

    @Autowired
    private IAIService aiService;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addKnowledge(AiKnowledge knowledge) {
        // 1. 保存文档
        knowledgeMapper.insert(knowledge);

        // 2. 生成文本向量
        List<Float> embedding;
        try {
            embedding = embeddingService.generateEmbedding(knowledge.getContent());
        } catch (Exception e) {
            throw new ServiceException("生成向量失败：" + e.getMessage());
        }

        // 3. 保存向量
        AiKnowledgeVector vector = new AiKnowledgeVector();
        vector.setKnowledgeId(knowledge.getId());
        try {
            vector.setEmbedding(objectMapper.writeValueAsString(embedding));
        } catch (JsonProcessingException e) {
            throw new ServiceException("向量序列化失败：" + e.getMessage());
        }
        vector.setKeywords(extractKeywords(knowledge.getContent())); // 简单关键词提取
        vectorMapper.insert(vector);
    }

    @Override
    public AiResponse knowledgeChat(AiChatRequest request) {
        // 1. 将用户问题转为向量
        List<Float> queryEmbedding;
        try {
            queryEmbedding = embeddingService.generateEmbedding(request.getQuestion());
        } catch (Exception e) {
            throw new ServiceException("问题向量生成失败：" + e.getMessage());
        }

        // 2. 向量检索相似文档（Top3）
        String queryEmbeddingJson;
        try {
            queryEmbeddingJson = objectMapper.writeValueAsString(queryEmbedding);
        } catch (JsonProcessingException e) {
            throw new ServiceException("问题向量序列化失败：" + e.getMessage());
        }
        List<Long> similarIds = vectorMapper.searchSimilar(queryEmbeddingJson, 3);
        if (similarIds.isEmpty()) {
            throw new ServiceException("未找到相关知识库内容");
        }

        // 3. 查询相似文档内容
        List<AiKnowledge> similarDocs = knowledgeMapper.selectByIds(similarIds);
        // 若指定分类，过滤结果
        if (StringUtils.hasText(request.getCategory())) {
            similarDocs = similarDocs.stream()
                    .filter(doc -> request.getCategory().equals(doc.getCategory()))
                    .collect(Collectors.toList());
        }

        // 4. 构建提示词（注入知识库信息）
        String prompt = buildPrompt(request.getQuestion(), similarDocs);

        // 5. 调用AI模型生成回答
        String answer = aiService.generate(prompt);

        AiResponse response = new AiResponse();
        response.setContent(answer);
        if (!similarDocs.isEmpty()) {
            response.setKnowledgeId(similarDocs.get(0).getId()); // 取最相关的文档ID
        }
        return response;
    }

    @Override
    public List<AiKnowledge> listByCategory(String category) {
        LambdaQueryWrapper<AiKnowledge> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(category)) {
            queryWrapper.eq(AiKnowledge::getCategory, category);
        }
        queryWrapper.orderByDesc(AiKnowledge::getCreateTime);
        return knowledgeMapper.selectList(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeKnowledge(Long id) {
        // 删除向量
        vectorMapper.delete(new LambdaQueryWrapper<AiKnowledgeVector>()
                .eq(AiKnowledgeVector::getKnowledgeId, id));
        // 删除文档
        return knowledgeMapper.deleteById(id) > 0;
    }

    /**
     * 构建带知识库的提示词
     */
    private String buildPrompt(String question, List<AiKnowledge> docs) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("基于以下知识库信息回答问题，不要编造内容：\n");
        for (AiKnowledge doc : docs) {
            prompt.append("- 文档标题：").append(doc.getTitle()).append("\n");
            prompt.append("  内容：").append(doc.getContent()).append("\n");
        }
        prompt.append("问题：").append(question).append("\n回答：");
        return prompt.toString();
    }

    /**
     * 简单关键词提取（实际项目可使用IKAnalyzer等工具）
     */
    private String extractKeywords(String content) {
        // 此处简化实现，实际需根据业务优化
        if (content.length() <= 20) {
            return content;
        }
        return content.substring(0, 20) + "...";
    }
}