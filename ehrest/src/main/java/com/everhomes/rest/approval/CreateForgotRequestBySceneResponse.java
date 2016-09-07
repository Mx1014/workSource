// @formatter:off
package com.everhomes.rest.approval;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>返回值：
 * <li>approvalRequest: 申请信息，参考{@link com.everhomes.rest.approval.BriefApprovalRequestDTO}</li>
 * </ul>
 */
public class CreateForgotRequestBySceneResponse {
	private BriefApprovalRequestDTO approvalRequest;

	public BriefApprovalRequestDTO getApprovalRequest() {
		return approvalRequest;
	}

	public void setApprovalRequest(BriefApprovalRequestDTO approvalRequest) {
		this.approvalRequest = approvalRequest;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
