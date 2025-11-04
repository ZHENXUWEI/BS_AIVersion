package com.ruoyi.web.controller.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * AI接口统一响应
 */
@Data
@ApiModel("AI接口响应结果")
public class AiResponse {
    @ApiModelProperty("响应内容")
    private String content;

    @ApiModelProperty("响应时间戳")
    private long timestamp = System.currentTimeMillis();
}
