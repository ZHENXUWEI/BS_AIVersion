package com.ruoyi.web.controller.api.service.serviceImpl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.HttpUtils; // 引入Ruoyi框架自带的HTTP工具类
import com.ruoyi.web.controller.api.dto.AiChatRequest;
import com.ruoyi.web.controller.api.dto.AiResponse;
import com.ruoyi.web.controller.api.dto.AiSearchRequest;
import com.ruoyi.web.controller.api.service.AiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class AiServiceImpl implements AiService {

    // 注入阿里百炼API配置（仅HTTP请求所需参数）
    @Value("${alibabacloud.bailian.url}")
    private String apiUrl;  // API地址：https://dashscope.aliyuncs.com/api/v1/services/aigc/text-generation/generation
    @Value("${alibabacloud.bailian.api-key}")
    private String accessKey;  // Bearer Token
    @Value("${alibabacloud.bailian.model}")
    private String model;  // 模型名称：如qwen-turbo

    @Override
    public AiResponse handleChat(AiChatRequest request) {
        log.info("AI问答请求: {}", request.getQuestion());
        // 直接调用HTTP请求方法（不涉及知识库）
        String answer = callAiApiByHttp(request.getQuestion());

        AiResponse response = new AiResponse();
        response.setContent(answer);
        return response;
    }

//    @Override
//    public AiResponse handleSearch(AiSearchRequest request) {
//        log.info("AI搜索请求: {}", request.getKeyword());
//        // 搜索接口同样直接调用AI（不关联知识库）
//        String answer = callAiApiByHttp(request.getKeyword());
//
//        AiResponse response = new AiResponse();
//        response.setContent(answer);
//        return response;
//    }

    /**
     * 纯HTTP请求调用AI接口（不依赖SDK，不使用知识库）
     */
    private String callAiApiByHttp(String question) {
        try {
            // 1. 构建请求体（按阿里百炼API要求的格式）
            Map<String, Object> requestBody = new HashMap<>();
            // 设置输入参数（prompt为用户问题）
            Map<String, String> input = new HashMap<>();
            input.put("prompt", question);
            requestBody.put("input", input);
            // 设置返回格式为文本
            Map<String, String> parameters = new HashMap<>();
            parameters.put("result_format", "text");
            parameters.put("max_tokens", String.valueOf(2048)); // 限制输出长度
            parameters.put("temperature", String.valueOf(0.7)); // 控制随机性
            requestBody.put("parameters", parameters);
            // 指定模型名称
            requestBody.put("model", model);

            // 2. 转换请求体为JSON字符串
            String jsonBody = JSON.toJSONString(requestBody);
            log.info("AI接口请求体: {}", jsonBody);

            // 3. 设置请求头（包含Bearer认证）
            Map<String, String> headers = new HashMap<>();
            headers.put("Content-Type", "application/json; charset=utf-8");
            headers.put("Authorization", "Bearer " + accessKey);  // 关键：Bearer Token认证

            // 4. 调用Ruoyi自带的HttpUtils发送POST请求
//            int aiTimeout = 20000;
            String responseStr = HttpUtils.sendPostWithHeaders(apiUrl, jsonBody, headers,120000);
            log.info("AI接口响应: {}", responseStr);

            // 5. 解析响应结果
            JSONObject responseJson = JSON.parseObject(responseStr);
            if (responseJson.containsKey("output") && responseJson.getJSONObject("output").containsKey("text")) {
                return responseJson.getJSONObject("output").getString("text");
            } else {
                // 错误处理
                String errorMsg = responseJson.getString("error") != null
                        ? responseJson.getString("error")
                        : "未知错误";
                log.error("AI接口调用失败: {}", errorMsg);
                return "AI服务异常：" + errorMsg;
            }
        } catch (Exception e) {
            log.error("HTTP请求调用失败", e);
            throw new ServiceException("调用AI服务失败: " + e.getMessage());
        }
    }
}