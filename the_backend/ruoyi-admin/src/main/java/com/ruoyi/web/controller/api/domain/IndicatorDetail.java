// com/ruoyi/web/controller/api/domain/IndicatorDetail.java
package com.ruoyi.web.controller.api.domain;

public class IndicatorDetail {
    private String name;
    private String key;
    private String type;
    private String threshold;
    private String value;
    private boolean status;
    private boolean required;
    private Integer id; // 新增：匹配字典ID

    // getter和setter方法（完整实现）
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getKey() { return key; }
    public void setKey(String key) { this.key = key; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getThreshold() { return threshold; }
    public void setThreshold(String threshold) { this.threshold = threshold; }

    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }

    public boolean isStatus() { return status; }
    public void setStatus(boolean status) { this.status = status; }

    public boolean isRequired() { return required; }
    public void setRequired(boolean required) { this.required = required; }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
}