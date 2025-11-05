package com.ruoyi.web.controller.api.service.serviceImpl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.HttpUtils;
import com.ruoyi.web.controller.api.dto.AiChatRequest;
import com.ruoyi.web.controller.api.dto.AiResponse;
import com.ruoyi.web.controller.api.dto.AiSearchRequest;
import com.ruoyi.web.controller.api.service.AiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class AiServiceImpl implements AiService {

    @Value("${aliyun.bailian.url:https://dashscope.aliyuncs.com/api/v1/services/aigc/text-generation/generation}")
    private String apiUrl;
    @Value("${aliyun.bailian.access-key:sk-f683165eb5dc45c08a4669b2c317d219}")
    private String accessKey;
    @Value("${aliyun.bailian.model:qwen-plus}")
    private String model;
//    @Value("${aliyun.bailian.knowledge-base-id}")
//    private String knowledgeBaseId;

    /**
     * 纯HTTP调用阿里百炼API（无SDK依赖，且不涉及知识库）
     */
    private String callAIByHttp(String question) {
        try {
            // 1. 构建请求体（确保不包含知识库相关参数）
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", model); // 仅指定模型

            // 构建消息列表
            List<Map<String, String>> messages = new ArrayList<>();
            Map<String, String> userMessage = new HashMap<>();
            userMessage.put("role", "user");
            userMessage.put("content", question);
            messages.add(userMessage);
            requestBody.put("messages", messages);

            // 其他参数（不添加任何知识库相关配置）
            requestBody.put("temperature", 0.7);
            requestBody.put("max_tokens", 2048);

            // 2. 构建请求头（仅包含认证信息）
            Map<String, String> headers = new HashMap<>();
            headers.put("Content-Type", "application/json; charset=utf-8");
            headers.put("Authorization", "Bearer " + accessKey);

            // 3. 发送HTTP POST请求
            String responseStr = HttpUtils.sendPostWithHeaders(
                    apiUrl,
                    JSON.toJSONString(requestBody),
                    headers
            );
            log.info("AI接口响应: {}", responseStr);

            // 4. 解析响应结果
            JSONObject responseJson = JSON.parseObject(responseStr);
            if (responseJson.containsKey("choices") && !responseJson.getJSONArray("choices").isEmpty()) {
                return responseJson.getJSONArray("choices")
                        .getJSONObject(0)
                        .getJSONObject("message")
                        .getString("content");
            } else {
                String errorMsg = responseJson.getString("error") != null
                        ? responseJson.getString("error")
                        : "未知错误";
                throw new ServiceException("AI接口返回异常: " + errorMsg);
            }
        } catch (Exception e) {
            log.error("AI调用失败", e);
            throw new ServiceException("调用AI服务失败: " + e.getMessage());
        }
    }

    @Override
    public AiResponse handleChat(AiChatRequest request) {
        log.info("阿里百炼知识库问答请求: {}", request.getQuestion());
        String answer = callAIByHttp(request.getQuestion());

        AiResponse response = new AiResponse();
        response.setContent(answer);
        return response;
    }

    @Override
    public AiResponse handleSearch(AiSearchRequest request) {
        String answer = callAIByHttp(request.getKeyword());

        AiResponse response = new AiResponse();
        response.setContent(answer);
        return response;
    }

//    /**
//     * 调用阿里百炼知识库接口（使用HttpUtils重构）
//     */
//    private String callBailianKnowledgeBase(String question) {
//        try {
//            // 构建请求参数
//            Map<String, Object> requestBody = new HashMap<>();
//            requestBody.put("model", model);
//            requestBody.put("knowledge_base_id", knowledgeBaseId); // 关联知识库
//
//            List<Map<String, String>> messages = new ArrayList<>();
//            Map<String, String> message = new HashMap<>();
//            message.put("role", "user");
//            message.put("content", question);
//            messages.add(message);
//            requestBody.put("messages", messages);
//
//            String jsonBody = JSON.toJSONString(requestBody);
//            log.info("知识库请求参数: {}", jsonBody);
//
//            // 设置请求头
//            Map<String, String> headers = new HashMap<>();
//            headers.put("Content-Type", "application/json");
//            headers.put("Authorization", "Bearer " + accessKey);
//
//            // 使用项目HttpUtils发送请求
//            String responseStr = HttpUtils.sendPostWithHeaders(apiUrl, jsonBody, headers);
//            log.info("知识库响应: {}", responseStr);
//
//            // 解析响应结果
//            com.alibaba.fastjson.JSONObject json = com.alibaba.fastjson.JSONObject.parseObject(responseStr);
//            if (json.containsKey("choices") && !json.getJSONArray("choices").isEmpty()) {
//                return json.getJSONArray("choices")
//                        .getJSONObject(0)
//                        .getJSONObject("message")
//                        .getString("content");
//            }
//            return "未获取到有效回答";
//        } catch (Exception e) {
//            log.error("阿里百炼调用异常", e);
//            return "查询失败：" + e.getMessage();
//        }
//    }
}