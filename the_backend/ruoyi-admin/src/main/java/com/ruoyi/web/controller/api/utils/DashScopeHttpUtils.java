package com.ruoyi.web.controller.api.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.utils.HttpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 直接通过HTTP调用DashScope API（不依赖SDK）
 */
public class DashScopeHttpUtils {
    private static final Logger log = LoggerFactory.getLogger(DashScopeHttpUtils.class);

    // DashScope API端点（以通义千问为例）
    private static final String DASHSCOPE_API_URL = "https://dashscope.aliyuncs.com/api/v1/services/aigc/text-generation/generation";

    // 替换为你的API密钥（建议从配置文件读取）
    private String apiKey;

    public DashScopeHttpUtils(String apiKey) {
        this.apiKey = apiKey;
    }

    /**
     * 调用DashScope大模型生成文本
     * @param prompt 用户输入的提示词
     * @param model 模型名称（如qwen-plus、qwen-turbo）
     * @return 模型返回的结果
     */
    public String generate(String prompt, String model) {
        try {
            // 1. 构建请求参数（按DashScope API格式）
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", model);

            // 构建消息列表（支持多轮对话）
            List<Map<String, String>> messages = new ArrayList<>();
            Map<String, String> userMessage = new HashMap<>();
            userMessage.put("role", "user");  // 角色：user/assistant
            userMessage.put("content", prompt);
            messages.add(userMessage);
            requestBody.put("messages", messages);

            // 可选参数：温度、最大 tokens 等
            requestBody.put("temperature", 0.7);
            requestBody.put("max_tokens", 2048);

            // 2. 构建请求头（包含认证信息）
            Map<String, String> headers = new HashMap<>();
            headers.put("Content-Type", "application/json");
            headers.put("Authorization", "Bearer " + apiKey);  // 认证格式：Bearer + 空格 + API密钥

            // 3. 调用项目中已有的HttpUtils发送POST请求
            String jsonParam = JSON.toJSONString(requestBody);
            String response = HttpUtils.sendPostWithHeaders(DASHSCOPE_API_URL, jsonParam, headers);

            // 4. 解析响应结果
            JSONObject responseJson = JSON.parseObject(response);
            if (responseJson.containsKey("output")) {
                return responseJson.getJSONObject("output")
                        .getJSONArray("choices")
                        .getJSONObject(0)
                        .getString("message");
            } else {
                String errorMsg = responseJson.getString("error_message");
                log.error("DashScope API调用失败: {}", errorMsg);
                return "调用失败：" + errorMsg;
            }
        } catch (Exception e) {
            log.error("DashScope HTTP调用异常", e);
            return "调用异常：" + e.getMessage();
        }
    }
}