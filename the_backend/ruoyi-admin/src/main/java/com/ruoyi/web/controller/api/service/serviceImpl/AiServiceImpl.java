// com.ruoyi.web.controller.api.service.serviceImpl.AiServiceImpl.java
package com.ruoyi.web.controller.api.service.serviceImpl;

import com.alibaba.dashscope.common.Message;
import com.alibaba.dashscope.aigc.generation.Generation;
import com.alibaba.dashscope.aigc.generation.GenerationParam;
import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.alibaba.dashscope.common.Role;
import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import java.util.ArrayList;
import java.util.List;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.DefaultAcsClient;
import com.ruoyi.web.controller.api.dto.AiChatRequest;
import com.ruoyi.web.controller.api.dto.AiResponse;
import com.ruoyi.web.controller.api.dto.AiSearchRequest;
import com.ruoyi.web.controller.api.service.AiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AiServiceImpl implements AiService {

    @Autowired
    private DefaultAcsClient bailianClient;

    @Value("${aliyun.bailian.knowledge-base-id}")
    private String knowledgeBaseId;

    @Value("${aliyun.bailian.model}")
    private String model;

    @Override
    public AiResponse handleChat(AiChatRequest request) {
        log.info("阿里百炼知识库问答请求: {}", request.getQuestion());
        String answer = callBailianKnowledgeBase(request.getQuestion());

        AiResponse response = new AiResponse();
        response.setContent(answer);
        return response;
    }

    @Override
    public AiResponse handleSearch(AiSearchRequest request) {
        // 若需要基于知识库的精准查询，可在此处扩展
        String answer = callBailianKnowledgeBase(request.getKeyword());

        AiResponse response = new AiResponse();
        response.setContent(answer);
        return response;
    }

    /**
     * 调用阿里百炼知识库接口
     */
    // 在 AiServiceImpl 中修改调用逻辑
    private String callBailianKnowledgeBase(String question) {
        try {
            // 初始化 Generation 客户端（指定百炼模型和知识库）
            Generation gen = new Generation();
            // 构造请求参数时使用字符串指定角色
            List<Message> messages = new ArrayList<>();
            messages.add(Message.builder()
                    .role("user")  // 直接传入字符串 "user"，而非 Role.USER 枚举
                    .content(question)
                    .build());

            GenerationParam param = GenerationParam.builder()
                    .model("qwen-plus")
                    .messages(messages)
                    .withKnowledgeBaseId("your_knowledge_base_id")  // 替换为实际知识库ID
                    .build();

            // 调用API
            GenerationResult result = gen.call(param);
            return result.getOutput().getChoices().get(0).getMessage().getContent();
        } catch (ApiException | InputRequiredException | NoApiKeyException e) {
            log.error("阿里百炼调用异常", e);
            return "查询失败：" + e.getMessage();
        }
    }
}