// com/ruoyi/web/controller/api/domain/MatchResult.java
package com.ruoyi.web.controller.api.domain;

import java.util.List;

public class MatchResult {
    private boolean success;
    private List<IndicatorDetail> details;

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public List<IndicatorDetail> getDetails() { return details; }
    public void setDetails(List<IndicatorDetail> details) { this.details = details; }
}