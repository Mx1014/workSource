// @formatter:off
package com.everhomes.rest.approval;

import java.util.List;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>返回值：
 * <li>nextPageAnchor: 下页锚点</li>
 * <li>approvalFlowLevelList: 审批级别列表，参考{@link com.everhomes.rest.approval.ApprovalFlowLevelDTO}</li>
 * </ul>
 */
public class ListApprovalFlowsResponse {
	private Long nextPageAnchor;
	private List<ApprovalFlowLevelDTO> approvalFlowLevelList;

	public List<ApprovalFlowLevelDTO> getApprovalFlowLevelList() {
		return approvalFlowLevelList;
	}

	public void setApprovalFlowLevelList(List<ApprovalFlowLevelDTO> approvalFlowLevelList) {
		this.approvalFlowLevelList = approvalFlowLevelList;
	}

	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}

	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
