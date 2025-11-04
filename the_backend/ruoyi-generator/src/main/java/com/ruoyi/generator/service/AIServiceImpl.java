package com.ruoyi.generator.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.generator.domain.dto.AiChatRequest;
import com.ruoyi.generator.domain.dto.AiResponse;
import com.ruoyi.generator.service.IAIService;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * AI模型服务实现（对接第三方API示例）
 * filePath: BS_AIVersion/the_backend/ruoyi-generator/src/main/java/com/ruoyi/generator/service/impl/AIServiceImpl.java
 */
@Service
@Slf4j  // 添加日志注解
public class AIServiceImpl implements IAIService {

    // 注入阿里百炼配置
    @Value("${alibabacloud.bailian.url}")
    private String apiUrl;
    @Value("${alibabacloud.bailian.access-key}")
    private String accessKey;  // 统一使用accessKey变量名
    @Value("${alibabacloud.bailian.model}")
    private String model;

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public String generate(String prompt) {
        try {
            Map<String, Object> param = new HashMap<>();
            param.put("prompt", prompt);
            param.put("max_tokens", 1024);

            // 设置请求头（使用accessKey变量）
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + accessKey);
            headers.set("Content-Type", "application/json");

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(param, headers);

            // 使用ParameterizedTypeReference解决泛型转换警告
            ResponseEntity<Map<String, Object>> responseEntity = restTemplate.exchange(
                    apiUrl,
                    HttpMethod.POST,
                    request,
                    new ParameterizedTypeReference<Map<String, Object>>() {}
            );
            Map<String, Object> response = responseEntity.getBody();

            // 解析返回结果（根据实际API格式调整）
            if (response == null || !response.containsKey("choices")) {
                throw new ServiceException("AI模型返回格式异常");
            }
            // 安全转换集合类型
            List<?> choices = (List<?>) response.get("choices");
            if (choices.isEmpty()) {
                throw new ServiceException("AI模型返回结果为空");
            }
            Map<String, Object> firstChoice = (Map<String, Object>) choices.get(0);
            return firstChoice.get("text").toString();
        } catch (Exception e) {
            throw new ServiceException("AI模型调用失败：" + e.getMessage());
        }
    }

    @Override  // 确保IAIService接口中存在此方法
    public AiResponse handleChat(AiChatRequest request) {
        log.info("AI问答请求: {}", request.getQuestion());
        String answer = callAlibabaBaiLianAPI(request.getQuestion());
        AiResponse response = new AiResponse();
        response.setContent(answer);
        return response;
    }

    /**
     * 调用阿里百炼API（核心实现）
     */
    private String callAlibabaBaiLianAPI(String question) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            // 1. 构建请求参数（按阿里百炼API文档格式）
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", model);
            List<Map<String, String>> messages = new ArrayList<>();
            // 修改为
            Map<String, String> messageMap = new HashMap<>();
            messageMap.put("role", "user");
            messageMap.put("content", question);
            messages.add(messageMap);
            requestBody.put("messages", messages);
            requestBody.put("temperature", 0.7);
            requestBody.put("max_tokens", 2048);

            // 2. 构建HTTP请求
            HttpPost httpPost = new HttpPost(apiUrl);
            httpPost.setHeader("Content-Type", "application/json; charset=utf-8");
            httpPost.setHeader("Authorization", "Bearer " + generateToken());
            httpPost.setEntity(new StringEntity(JSON.toJSONString(requestBody), StandardCharsets.UTF_8));

            // 3. 发送请求并解析响应
            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                String responseStr = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
                log.info("阿里百炼API响应: {}", responseStr);

                JSONObject json = JSON.parseObject(responseStr);
                if (json.containsKey("choices")) {
                    return json.getJSONArray("choices")
                            .getJSONObject(0)
                            .getJSONObject("message")
                            .getString("content");
                } else {
                    log.error("API调用失败: {}", json.getString("error"));
                    return "AI服务异常：" + json.getString("error");
                }
            }
        } catch (Exception e) {
            log.error("调用阿里百炼API出错", e);
            return "调用AI服务失败: " + e.getMessage();
        }
    }

    /**
     * 生成阿里百炼认证Token（需根据官方文档实现）
     */
    private String generateToken() {
        // 示例：实际需使用accessKey和secret生成签名
        // 参考阿里云SDK：com.aliyun.auth.credentials.Credential
        return "generated_token_based_on_ak_sk";
    }
}