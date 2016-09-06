// @formatter:off
package com.everhomes.rest.approval;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>返回值：
 * <li>approvalRequest: 申请信息，参考{@link com.everhomes.rest.approval.ApprovalRequestDTO}</li>
 * </ul>
 */
public class CreateForgotRequestBySceneResponse {
	private ApprovalRequestDTO approvalRequest;

	public ApprovalRequestDTO getApprovalRequest() {
		return approvalRequest;
	}

	public void setApprovalRequest(ApprovalRequestDTO approvalRequest) {
		this.approvalRequest = approvalRequest;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
