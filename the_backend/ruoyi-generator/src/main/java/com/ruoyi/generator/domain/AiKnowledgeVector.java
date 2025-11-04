package com.ruoyi.generator.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

// 向量存储实体（用于快速检索）
@Data
@TableName("ai_knowledge_vector")
public class AiKnowledgeVector {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long knowledgeId; // 关联知识库ID
    private String embedding; // 向量数据（JSON格式存储）
    private String keywords; // 关键词（辅助检索）
}
