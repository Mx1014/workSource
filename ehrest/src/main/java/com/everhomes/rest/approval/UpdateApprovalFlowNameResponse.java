// @formatter:off
package com.everhomes.rest.approval;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>返回值：
 * <li>approvalFlow: 审批流程，参考{@link com.everhomes.rest.approval.ApprovalFlowDTO}</li>
 * </ul>
 */
public class UpdateApprovalFlowNameResponse {
	private ApprovalFlowDTO approvalFlow;

	public ApprovalFlowDTO getApprovalFlow() {
		return approvalFlow;
	}

	public void setApprovalFlow(ApprovalFlowDTO approvalFlow) {
		this.approvalFlow = approvalFlow;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
