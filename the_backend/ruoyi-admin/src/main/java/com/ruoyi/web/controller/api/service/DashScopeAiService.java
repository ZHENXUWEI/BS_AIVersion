package com.ruoyi.web.controller.api.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@ConditionalOnBean(ChatModel.class) // 只有当 ChatModel bean 存在时才创建
public class DashScopeAiService {

    private final ChatClient chatClient;

    public DashScopeAiService(ChatModel chatModel) {
        this.chatClient = ChatClient.create(chatModel);
    }

    /**
     * 基础问答调用
     */
    public String chat(String question) {
        return chatClient.prompt(question)
                .call()
                .content();
    }

    /**
     * 带动态参数的问答
     */
    public String chatWithContext(String question, String context) {
        String template = "基于以下上下文回答问题：\n{context}\n问题：{question}";

        PromptTemplate promptTemplate = new PromptTemplate(template);
        Prompt prompt = promptTemplate.create(Map.of("context", context, "question", question));

        return chatClient.prompt(prompt)
                .call()
                .content();
    }
}