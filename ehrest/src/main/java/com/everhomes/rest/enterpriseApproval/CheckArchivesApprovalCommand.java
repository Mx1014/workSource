package com.everhomes.rest.enterpriseApproval;

/**
 * <ul>
 * <li>userId: 用户id</li>
 * <li>organizationId: 公司id</li>
 * <li>approvalId: 审批id</li>
 * </ul>
 */
public class CheckArchivesApprovalCommand {

    private Long userId;

    private Long organizationId;

    private Long approvalId;

    public CheckArchivesApprovalCommand() {
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }


    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public Long getApprovalId() {
        return approvalId;
    }

    public void setApprovalId(Long approvalId) {
        this.approvalId = approvalId;
    }
}
