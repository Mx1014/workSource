// @formatter:off
package com.everhomes.rest.approval;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>返回值：
 * <li>approvalBasicInfoOfRequest: 审批基本信息，参考{@link com.everhomes.rest.approval.ApprovalBasicInfoOfRequestDTO}</li>
 * </ul>
 */
public class GetApprovalBasicInfoOfRequestBySceneResponse {
	private ApprovalBasicInfoOfRequestDTO approvalBasicInfoOfRequest;

	public GetApprovalBasicInfoOfRequestBySceneResponse(ApprovalBasicInfoOfRequestDTO approvalBasicInfoOfRequest) {
		super();
		this.approvalBasicInfoOfRequest = approvalBasicInfoOfRequest;
	}

	public ApprovalBasicInfoOfRequestDTO getApprovalBasicInfoOfRequest() {
		return approvalBasicInfoOfRequest;
	}

	public void setApprovalBasicInfoOfRequest(ApprovalBasicInfoOfRequestDTO approvalBasicInfoOfRequest) {
		this.approvalBasicInfoOfRequest = approvalBasicInfoOfRequest;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
