// @formatter:off
package com.everhomes.rest.approval;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>返回值：
 * <li>approvalFlow: 审批流程，参考{@link com.everhomes.rest.approval.BriefApprovalFlowDTO}</li>
 * </ul>
 */
public class UpdateApprovalFlowInfoResponse {
	private BriefApprovalFlowDTO briefApprovalFlow;

	public UpdateApprovalFlowInfoResponse(BriefApprovalFlowDTO briefApprovalFlow) {
		super();
		this.briefApprovalFlow = briefApprovalFlow;
	}

	public BriefApprovalFlowDTO getBriefApprovalFlow() {
		return briefApprovalFlow;
	}

	public void setBriefApprovalFlow(BriefApprovalFlowDTO briefApprovalFlow) {
		this.briefApprovalFlow = briefApprovalFlow;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
