package com.ruoyi.web.controller.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * AI查询请求参数
 */
@Data
@ApiModel("AI查询请求参数")
public class AiSearchRequest {
    @NotBlank(message = "查询关键词不能为空")
    @ApiModelProperty(value = "查询关键词", required = true)
    private String keyword;

    @ApiModelProperty(value = "查询类型(policy:政策文件,enterprise:企业数据,general:通用问答)", example = "policy")
    private String type;

    // getter和setter
    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
