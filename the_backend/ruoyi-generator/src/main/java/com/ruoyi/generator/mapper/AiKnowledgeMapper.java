package com.ruoyi.generator.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.generator.domain.AiKnowledge;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 知识库Mapper
 * filePath: BS_AIVersion/the_backend/ruoyi-generator/src/main/java/com/ruoyi/generator/mapper/AiKnowledgeMapper.java
 */
@Mapper
public interface AiKnowledgeMapper extends BaseMapper<AiKnowledge> {

    /**
     * 根据分类查询知识库
     */
    List<AiKnowledge> selectByCategory(@Param("category") String category);

    /**
     * 批量查询知识库
     */
    List<AiKnowledge> selectByIds(@Param("ids") List<Long> ids);
}