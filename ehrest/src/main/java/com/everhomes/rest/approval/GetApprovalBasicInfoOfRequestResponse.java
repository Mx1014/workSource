// @formatter:off
package com.everhomes.rest.approval;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>返回值：
 * <li>approvalBasicInfo: 审批基本信息，参考{@link com.everhomes.rest.approval.ApprovalBasicInfoOfRequestDTO}</li>
 * </ul>
 */
public class GetApprovalBasicInfoOfRequestResponse {
	private ApprovalBasicInfoOfRequestDTO approvalBasicInfoOfRequest;

	public ApprovalBasicInfoOfRequestDTO getApprovalBasicInfo() {
		return approvalBasicInfoOfRequest;
	}

	public void setApprovalBasicInfo(ApprovalBasicInfoOfRequestDTO approvalBasicInfo) {
		this.approvalBasicInfoOfRequest = approvalBasicInfo;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
