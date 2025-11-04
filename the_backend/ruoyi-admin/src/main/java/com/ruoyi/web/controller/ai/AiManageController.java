package com.ruoyi.web.controller.ai;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.web.controller.api.dto.AiChatRequest;
import com.ruoyi.web.controller.api.dto.AiResponse;
import com.ruoyi.web.controller.api.dto.AiSearchRequest;
import com.ruoyi.web.controller.api.service.AiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

// 示例控制器
@RestController
@RequestMapping("/api/ai")
public class AiManageController extends BaseController {

    @Autowired
    private AiService aiService;

    // AI问答接口
    @PostMapping("/chat")
    public AjaxResult chat(AiChatRequest request) {
        AiResponse response = aiService.handleChat(request); // 直接传递DTO对象
        return AjaxResult.success(Collections.singletonMap("answer", response.getContent()));
    }

    // AI查询接口
    @GetMapping("/search")
    public AjaxResult search(AiSearchRequest request) {
        AiResponse response = aiService.handleSearch(request); // 直接传递DTO对象
        return AjaxResult.success(Collections.singletonMap("result", response.getContent()));
    }
}
