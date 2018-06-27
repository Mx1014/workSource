package com.everhomes.rest.enterpriseApproval;

/**
 * <ul>
 * <li>sourceId: 过滤id</li>
 * <li>sourceType: 过滤类型: APPROVAL-审批 GROUP-审批组</li>
 * </ul>
 */
public class ApprovalFilter {

    private Long sourceId;

    private String sourceType;

    public ApprovalFilter() {
    }

    public Long getSourceId() {
        return sourceId;
    }

    public void setSourceId(Long sourceId) {
        this.sourceId = sourceId;
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }
}
