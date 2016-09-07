// @formatter:off
package com.everhomes.rest.approval;

import java.util.List;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * 返回值：
 * <li>nextPageAnchor: 下页锚点</li>
 * <li>approvalRequestList: 请假列表，参考{@link com.everhomes.rest.approval.BriefApprovalRequestDTO}</li>
 * </ul>
 */
public class ListApprovalRequestBySceneResponse {
	private Long nextPageAnchor;
	private List<BriefApprovalRequestDTO> approvalRequestList;

	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}

	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}

	public List<BriefApprovalRequestDTO> getApprovalRequestList() {
		return approvalRequestList;
	}

	public void setApprovalRequestList(List<BriefApprovalRequestDTO> approvalRequestList) {
		this.approvalRequestList = approvalRequestList;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
