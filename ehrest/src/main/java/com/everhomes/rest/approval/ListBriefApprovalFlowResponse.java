package com.everhomes.rest.approval;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>返回值
 * <li>approvalFlowList: 简短的审批流程列表</li>
 * </ul>
 */
public class ListBriefApprovalFlowResponse {
	@ItemType(BriefApprovalFlowDTO.class)
	private List<BriefApprovalFlowDTO> approvalFlowList;

	public List<BriefApprovalFlowDTO> getApprovalFlowList() {
		return approvalFlowList;
	}

	public void setApprovalFlowList(List<BriefApprovalFlowDTO> approvalFlowList) {
		this.approvalFlowList = approvalFlowList;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
