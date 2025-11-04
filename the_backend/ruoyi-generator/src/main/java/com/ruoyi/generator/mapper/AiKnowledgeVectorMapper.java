package com.ruoyi.generator.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.generator.domain.AiKnowledge;
import com.ruoyi.generator.domain.AiKnowledgeVector;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 知识库向量Mapper
 * filePath: BS_AIVersion/the_backend/ruoyi-generator/src/main/java/com/ruoyi/generator/mapper/AiKnowledgeVectorMapper.java
 */
@Mapper
public interface AiKnowledgeVectorMapper extends BaseMapper<AiKnowledgeVector> {

    /**
     * 检索相似向量（返回TopN知识库）
     * 注意：实际项目需结合向量数据库实现，此处为MyBatis示例
     */
    List<Long> searchSimilar(@Param("embedding") String queryEmbeddingJson, @Param("topN") Integer topN);
}