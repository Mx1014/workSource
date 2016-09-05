// @formatter:off
package com.everhomes.rest.approval;

import java.util.List;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * 返回值：
 * <li>nextPageAnchor: 下页锚点</li>
 * <li>askForLeaveRequestList: 请假列表，参考{@link com.everhomes.rest.approval.ApprovalRequestDTO}</li>
 * </ul>
 */
public class ListApprovalRequestBySceneResponse {
	private Long nextPageAnchor;
	private List<ApprovalRequestDTO> approvalRequestList;

	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}

	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}

	public List<ApprovalRequestDTO> getApprovalRequestList() {
		return approvalRequestList;
	}

	public void setApprovalRequestList(List<ApprovalRequestDTO> approvalRequestList) {
		this.approvalRequestList = approvalRequestList;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
