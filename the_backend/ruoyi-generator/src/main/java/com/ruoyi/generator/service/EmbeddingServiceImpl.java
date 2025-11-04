package com.ruoyi.generator.service;

import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.utils.HttpUtils;
import com.ruoyi.generator.service.IEmbeddingService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 向量生成服务实现（对接阿里百炼API）
 */
@Service
public class EmbeddingServiceImpl implements IEmbeddingService {

    // 阿里百炼API密钥（从配置文件读取）
    @Value("${alibabacloud.bailian.access-key}")
    private String apiKey;

//    @Value("${alibabacloud.bailian.api-secret}")
//    private String apiSecret;

    // 阿里百炼Embedding接口地址（具体以官方文档为准）
    private static final String EMBEDDING_URL = "https://bailian.aliyuncs.com/v1/embeddings/text-embedding-v1";

    @Override
    public List<Float> generateEmbedding(String text) throws Exception {
        if (!StringUtils.hasText(text)) {
            throw new IllegalArgumentException("文本内容不能为空");
        }

        // 1. 构建请求参数
        Map<String, Object> param = new HashMap<>();
        param.put("input", text);  // 阿里百炼通常使用input字段接收文本

        // 2. 构建请求头（包含认证信息）
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("X-API-Key", apiKey);
//        headers.put("X-API-Secret", apiSecret);

        // 3. 调用阿里百炼Embedding API
        String response = HttpUtils.sendPostWithHeaders(EMBEDDING_URL,
                JSONObject.toJSONString(param),
                headers);
        JSONObject result = JSONObject.parseObject(response);

        // 4. 解析返回结果（根据阿里百炼实际返回格式调整）
        if (result.containsKey("code") && !"200".equals(result.getString("code"))) {
            throw new Exception("向量生成失败：" + result.getString("message"));
        }

        // 假设返回格式为 { "data": { "embedding": [0.1, 0.2, ...] } }
        return result.getJSONObject("data")
                .getJSONArray("embedding")
                .toJavaList(Float.class);
    }
}