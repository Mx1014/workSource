// @formatter:off
package com.everhomes.rest.approval;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>返回值
 * <li>approvalFlowOfRequestList: 申请的审批流程列表，参考{@link com.everhomes.rest.approval.ApprovalFlowOfRequestDTO}</li>
 * </ul>
 */
public class ListApprovalFlowOfRequestResponse {
	@ItemType(ApprovalFlowOfRequestDTO.class)
	private List<ApprovalFlowOfRequestDTO> approvalFlowOfRequestList;

	public List<ApprovalFlowOfRequestDTO> getApprovalFlowOfRequestList() {
		return approvalFlowOfRequestList;
	}

	public void setApprovalFlowOfRequestList(List<ApprovalFlowOfRequestDTO> approvalFlowOfRequestList) {
		this.approvalFlowOfRequestList = approvalFlowOfRequestList;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
