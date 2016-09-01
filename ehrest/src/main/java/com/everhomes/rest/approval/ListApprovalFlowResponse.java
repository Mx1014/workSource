// @formatter:off
package com.everhomes.rest.approval;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>返回值：
 * <li>nextPageAnchor: 下页锚点</li>
 * <li>approvalFlowList: 审批流程列表，参考{@link com.everhomes.rest.approval.ApprovalFlowDTO}</li>
 * </ul>
 */
public class ListApprovalFlowResponse {
	private Long nextPageAnchor;
	private List<ApprovalFlowDTO> approvalFlowList;

	public ListApprovalFlowResponse(Long nextPageAnchor, List<ApprovalFlowDTO> approvalFlowList) {
		super();
		this.nextPageAnchor = nextPageAnchor;
		this.approvalFlowList = approvalFlowList;
	}

	public List<ApprovalFlowDTO> getApprovalFlowList() {
		return approvalFlowList;
	}

	public void setApprovalFlowList(List<ApprovalFlowDTO> approvalFlowList) {
		this.approvalFlowList = approvalFlowList;
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
