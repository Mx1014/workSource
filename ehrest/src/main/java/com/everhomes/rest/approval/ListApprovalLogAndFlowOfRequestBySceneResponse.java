// @formatter:off
package com.everhomes.rest.approval;

import java.util.List;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>返回值：
 * <li>approvalLogAndFlowOfRequestList: 申请的审批日志 与审批流程列表，参考{@link com.everhomes.rest.approval.ApprovalLogAndFlowOfRequestDTO}</li>
 * </ul>
 */
public class ListApprovalLogAndFlowOfRequestBySceneResponse {
	private List<ApprovalLogAndFlowOfRequestDTO> approvalLogAndFlowOfRequestList;

	public List<ApprovalLogAndFlowOfRequestDTO> getApprovalLogAndFlowOfRequestList() {
		return approvalLogAndFlowOfRequestList;
	}

	public void setApprovalLogAndFlowOfRequestList(List<ApprovalLogAndFlowOfRequestDTO> approvalLogAndFlowOfRequestList) {
		this.approvalLogAndFlowOfRequestList = approvalLogAndFlowOfRequestList;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
