package com.everhomes.rest.decoration;

/**
 * <ul>
 * <li>status：审批状态 参考{@link com.everhomes.rest.flow.FlowCaseStatus}</li>
 * <li>approvalName</li>
 * <li>flowCaseId</li>
 * </ul>
 */
public class DecorationFlowCaseDTO {

    private String approvalName;
    private Long flowCaseId;
    private Byte status;

    public Long getFlowCaseId() {
        return flowCaseId;
    }

    public void setFlowCaseId(Long flowCaseId) {
        this.flowCaseId = flowCaseId;
    }

    public String getApprovalName() {
        return approvalName;
    }

    public void setApprovalName(String approvalName) {
        this.approvalName = approvalName;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }
}
