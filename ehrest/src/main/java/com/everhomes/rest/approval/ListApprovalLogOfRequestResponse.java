// @formatter:off
package com.everhomes.rest.approval;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>返回值：
 * <li>approvalLogOfRequestList: 申请的审批日志列表，参考{@link com.everhomes.rest.approval.ApprovalLogOfRequestDTO}</li>
 * </ul>
 */
public class ListApprovalLogOfRequestResponse {
	@ItemType(ApprovalLogOfRequestDTO.class)
	private List<ApprovalLogOfRequestDTO> approvalLogOfRequestList;

	public ListApprovalLogOfRequestResponse(List<ApprovalLogOfRequestDTO> approvalLogOfRequestList) {
		super();
		this.approvalLogOfRequestList = approvalLogOfRequestList;
	}

	public List<ApprovalLogOfRequestDTO> getApprovalLogOfRequestList() {
		return approvalLogOfRequestList;
	}

	public void setApprovalLogOfRequestList(List<ApprovalLogOfRequestDTO> approvalLogOfRequestList) {
		this.approvalLogOfRequestList = approvalLogOfRequestList;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
