package com.ruoyi.web.controller.api.service;

import com.ruoyi.web.controller.api.dto.AiChatRequest;
import com.ruoyi.web.controller.api.dto.AiResponse;
import com.ruoyi.web.controller.api.dto.AiSearchRequest;

/**
 * AI服务接口
 */
public interface AiService {

    /**
     * 处理AI问答
     * @param request 问答请求参数
     * @return 问答响应结果
     */
    AiResponse handleChat(AiChatRequest request);

    /**
     * 处理AI查询
     * @param request 查询请求参数
     * @return 查询响应结果
     */
    AiResponse handleSearch(AiSearchRequest request);
}