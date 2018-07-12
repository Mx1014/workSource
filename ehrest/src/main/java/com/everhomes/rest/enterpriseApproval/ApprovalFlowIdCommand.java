package com.everhomes.rest.enterpriseApproval;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>flowCaseId: 工作流id</li>
 * </ul>
 */
public class ApprovalFlowIdCommand {

    private Long flowCaseId;

    public ApprovalFlowIdCommand() {
    }

    public ApprovalFlowIdCommand(Long flowCaseId) {
        this.flowCaseId = flowCaseId;
    }

    public Long getFlowCaseId() {
        return flowCaseId;
    }

    public void setFlowCaseId(Long flowCaseId) {
        this.flowCaseId = flowCaseId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
