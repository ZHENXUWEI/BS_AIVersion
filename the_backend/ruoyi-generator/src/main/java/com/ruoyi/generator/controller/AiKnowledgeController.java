package com.ruoyi.generator.controller;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.generator.domain.AiKnowledge;
import com.ruoyi.generator.domain.dto.AiChatRequest;
import com.ruoyi.generator.domain.dto.AiResponse;
import com.ruoyi.generator.service.IAiKnowledgeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Collections;

/**
 * 知识库控制器
 * filePath: BS_AIVersion/the_backend/ruoyi-generator/src/main/java/com/ruoyi/generator/controller/AiKnowledgeController.java
 */
@RestController
@RequestMapping("/api/ai/knowledge")
public class AiKnowledgeController extends BaseController {

    @Autowired
    private IAiKnowledgeService knowledgeService;

    /**
     * 新增知识库文档
     */
    @PreAuthorize("@ss.hasPermi('ai:knowledge:add')")
    @PostMapping("/add")
    public AjaxResult addKnowledge(@Validated @RequestBody AiKnowledge knowledge) {
        knowledgeService.addKnowledge(knowledge);
        return AjaxResult.success();
    }

    /**
     * 基于知识库的问答
     */
    @PostMapping("/chat")
    public AjaxResult knowledgeChat(@Validated @RequestBody AiChatRequest request) {
        AiResponse response = knowledgeService.knowledgeChat(request);
        return AjaxResult.success(Collections.singletonMap("answer", response.getContent()));
    }

    /**
     * 知识库文档列表查询
     */
    @PreAuthorize("@ss.hasPermi('ai:knowledge:list')")
    @GetMapping("/list")
    public AjaxResult listKnowledge(@RequestParam(required = false) String category) {
        List<AiKnowledge> list = knowledgeService.listByCategory(category);
        return AjaxResult.success(list);
    }

    /**
     * 删除知识库文档
     */
    @PreAuthorize("@ss.hasPermi('ai:knowledge:remove')")
    @DeleteMapping("/{id}")
    public AjaxResult removeKnowledge(@PathVariable Long id) {
        return toAjax(knowledgeService.removeKnowledge(id) ? 1 : 0);
    }
}