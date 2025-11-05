// com.ruoyi.web.controller.api.controller.AiController.java
package com.ruoyi.web.controller.api.controller;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.web.controller.api.common.Result;
import com.ruoyi.web.controller.api.dto.AiChatRequest;
import com.ruoyi.web.controller.api.dto.AiResponse;
import com.ruoyi.web.controller.api.dto.AiSearchRequest;
import com.ruoyi.web.controller.api.service.AiService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * AI功能接口控制器
 * 对应前端AI问答和AI查询页面
 */
@RestController
@RequestMapping("/api/ai")
@Api(tags = "AI功能接口")
public class ApiAiController {

    @Autowired
    private AiService aiService;

    /**
     * AI问答接口
     * 处理用户与AI的对话交互
     */
    @PostMapping("/chat")
    @ApiOperation("AI问答接口")
//    public Result<Map<String, Object>> chat(@Validated @RequestBody AiChatRequest request) {
//        AiResponse response = aiService.handleChat(request);
//        Map<String, Object> result = new HashMap<>();
//        result.put("content", response.getContent());
//        result.put("timestamp", response.getTimestamp());
//        return Result.success(result); // 确保前端能正确解析res.data.data.content
//    }
    public AjaxResult chat(@Validated @RequestBody AiChatRequest request) { // 添加@RequestBody注解
        AiResponse response = aiService.handleChat(request);
        return AjaxResult.success(Collections.singletonMap("answer", response.getContent()));
    }

    /**
     * 流式AI问答接口
     */
    @PostMapping(value = "/chats", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> chatStream(@RequestBody AiChatRequest request) {
        // 调用服务获取完整回答
        String fullAnswer = aiService.handleChat(request).getContent();

        // 模拟流式输出（实际项目中应从AI服务直接获取流）
        return Flux.fromArray(fullAnswer.split(""))
                .delayElements(Duration.ofMillis(50)); // 控制打字速度
    }

//    /**
//     * AI查询接口
//     * 处理基于关键词的精准查询
//     */
//    @GetMapping("/api-search")
//    @ApiOperation("AI查询接口")
//    public Result<AiResponse> search(@Validated AiSearchRequest request) {
//        AiResponse response = aiService.handleSearch(request);
//        return Result.success(response);
//    }
}