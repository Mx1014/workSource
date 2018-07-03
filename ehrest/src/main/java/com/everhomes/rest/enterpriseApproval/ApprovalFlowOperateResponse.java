package com.everhomes.rest.enterpriseApproval;

/**
 * <ul>
 * <li>failedCounts: 失败总数</li>
 * <li>successfulCounts: 成功总数</li>
 * </ul>
 */
public class ApprovalFlowOperateResponse {

    private Integer failedCounts;

    private Integer successfulCounts;

    public ApprovalFlowOperateResponse() {
        this.failedCounts = 0;
        this.successfulCounts = 0;
    }

    public Integer getFailedCounts() {
        return failedCounts;
    }

    public void setFailedCounts(Integer failedCounts) {
        this.failedCounts = failedCounts;
    }

    public Integer getSuccessfulCounts() {
        return successfulCounts;
    }

    public void setSuccessfulCounts(Integer successfulCounts) {
        this.successfulCounts = successfulCounts;
    }
}
