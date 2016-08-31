// @formatter:off
package com.everhomes.rest.approval;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>返回值：
 * <li>approvalFlowLevel: 审批级别，参考{@link com.everhomes.rest.approval.ApprovalFlowLevelDTO}</li>
 * </ul>
 */
public class CreateApprovalFlowLevelResponse {
	private ApprovalFlowLevelDTO approvalFlowLevel;

	public ApprovalFlowLevelDTO getApprovalFlowLevel() {
		return approvalFlowLevel;
	}

	public void setApprovalFlowLevel(ApprovalFlowLevelDTO approvalFlowLevel) {
		this.approvalFlowLevel = approvalFlowLevel;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	
}
