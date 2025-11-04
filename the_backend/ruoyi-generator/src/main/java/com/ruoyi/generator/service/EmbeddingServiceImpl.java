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
 * 向量生成服务实现（对接百度文心API示例）
 * filePath: BS_AIVersion/the_backend/ruoyi-generator/src/main/java/com/ruoyi/generator/service/impl/EmbeddingServiceImpl.java
 */
@Service
public class EmbeddingServiceImpl implements IEmbeddingService {

    @Value("${baidu.ai.api-key}")
    private String apiKey;

    @Value("${baidu.ai.secret-key}")
    private String secretKey;

    private static final String ACCESS_TOKEN_URL = "https://aip.baidubce.com/oauth/2.0/token";
    private static final String EMBEDDING_URL = "https://aip.baidubce.com/rpc/2.0/ai_custom/v1/text_embedding/embedding-v1";

    @Override
    public List<Float> generateEmbedding(String text) throws Exception {
        if (!StringUtils.hasText(text)) {
            throw new IllegalArgumentException("文本内容不能为空");
        }

        // 1. 获取访问令牌
        String accessToken = getAccessToken();

        // 2. 调用向量生成API
        String url = EMBEDDING_URL + "?access_token=" + accessToken;
        Map<String, Object> param = new HashMap<>();
        param.put("text", text);

        String response = HttpUtils.post(url, JSONObject.toJSONString(param), "application/json");
        JSONObject result = JSONObject.parseObject(response);

        // 3. 解析向量结果（根据实际API返回格式调整）
        if (result.containsKey("error_code")) {
            throw new Exception("向量生成失败：" + result.getString("error_msg"));
        }
        return result.getJSONArray("data").toJavaList(Float.class);
    }

    /**
     * 获取百度AI访问令牌
     */
    private String getAccessToken() throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("grant_type", "client_credentials");
        params.put("client_id", apiKey);
        params.put("client_secret", secretKey);

        String response = HttpUtils.get(ACCESS_TOKEN_URL, params);
        JSONObject result = JSONObject.parseObject(response);
        if (result.containsKey("error")) {
            throw new Exception("获取令牌失败：" + result.getString("error_description"));
        }
        return result.getString("access_token");
    }
}