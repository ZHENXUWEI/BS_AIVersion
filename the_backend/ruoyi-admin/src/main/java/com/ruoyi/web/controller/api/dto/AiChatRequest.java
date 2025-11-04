// com.ruoyi.web.controller.api.dto.AiChatRequest.java
package com.ruoyi.web.controller.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * AI问答请求参数
 */
@Data
@ApiModel("AI问答请求参数")
public class AiChatRequest {
    @NotBlank(message = "问题内容不能为空")
    @ApiModelProperty(value = "用户问题", required = true)
    private String question;

    // getter和setter
    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }
}
