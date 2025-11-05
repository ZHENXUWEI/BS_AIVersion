package com.ruoyi.web.controller.api.config;

//import org.springframework.ai.dashscope.DashScopeChatModel;
//import org.springframework.ai.dashscope.DashScopeChatOptions;
//import org.springframework.ai.chat.model.ChatModel;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;

/**
 * AI模型配置类，注册ChatModel类型的Bean供自动装配
 */
//@Configuration
public class AiModelConfig {

//    /**
//     * 注册DashScope的ChatModel Bean，解决自动装配找不到Bean的问题
//     */
//    @Bean
//    public ChatModel dashScopeChatModel(
//            @Value("${spring.ai.dashscope.api-key}") String apiKey,
//            @Value("${spring.ai.dashscope.model-name:qwen-plus}") String modelName,
//            @Value("${spring.ai.dashscope.temperature:0.7}") Float temperature,
//            @Value("${spring.ai.dashscope.max-tokens:2048}") Integer maxTokens) {
//
//        // 构建DashScope模型配置
//        DashScopeChatOptions options = DashScopeChatOptions.builder()
//                .apiKey(apiKey)
//                .model(modelName)
//                .temperature(temperature)
//                .maxTokens(maxTokens)
//                // 可根据需要添加其他参数（如topP、stop等）
//                .build();
//
//        // 创建并返回ChatModel实例
//        return new DashScopeChatModel(options);
//    }
}