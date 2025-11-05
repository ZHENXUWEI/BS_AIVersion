package com.ruoyi.web.controller.api.config;

//import org.springframework.ai.chat.model.ChatModel;
//import org.springframework.ai.dashscope.DashScopeChatModel;
//import org.springframework.ai.dashscope.DashScopeChatOptions;
//import org.springframework.ai.dashscope.api.DashScopeService;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;

/**
 * 阿里AI模型配置类
 */
//@Configuration
public class AliAiConfig {

//    @Value("${spring.ai.dashscope.api-key}")
//    private String apiKey;
//
//    @Value("${spring.ai.dashscope.chat.options.model:qwen-plus}")
//    private String modelName;
//
//    /**
//     * 使用 Spring AI 原生的 DashScope 配置
//     */
//    @Bean
//    public ChatModel dashScopeChatModel() {
//        DashScopeService dashScopeService = new DashScopeService(apiKey);
//        DashScopeChatOptions options = DashScopeChatOptions.builder()
//                .withModel(modelName)
//                .withTemperature(0.7f)
//                .build();
//
//        return new DashScopeChatModel(dashScopeService, options);
//    }
}