// @formatter:off
package com.everhomes.rest.approval;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>返回值：
 * <li>approvalBasicInfo: 审批基本信息</li>
 * </ul>
 */
public class GetApprovalBasicInfoBySceneResponse {
	private ApprovalBasicInfoDTO approvalBasicInfo;

	public ApprovalBasicInfoDTO getApprovalBasicInfo() {
		return approvalBasicInfo;
	}

	public void setApprovalBasicInfo(ApprovalBasicInfoDTO approvalBasicInfo) {
		this.approvalBasicInfo = approvalBasicInfo;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
