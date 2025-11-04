package com.ruoyi.generator.service;

import java.util.List;

/**
 * 向量生成服务接口
 * filePath: BS_AIVersion/the_backend/ruoyi-generator/src/main/java/com/ruoyi/generator/service/IEmbeddingService.java
 */
public interface IEmbeddingService {

    /**
     * 生成文本向量
     * @param text 文本内容
     * @return 向量列表
     */
    List<Float> generateEmbedding(String text) throws Exception;
}