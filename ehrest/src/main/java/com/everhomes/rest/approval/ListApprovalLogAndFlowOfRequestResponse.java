// @formatter:off
package com.everhomes.rest.approval;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>返回值：
 * 
 * <li>title: 申请的标题文字-根据类型不同,文字不同</li>
 * <li>approvalLogAndFlowOfRequestList: 申请的审批日志 与审批流程列表，参考{@link com.everhomes.rest.approval.ApprovalLogAndFlowOfRequestDTO}</li>
 * </ul>
 */
public class ListApprovalLogAndFlowOfRequestResponse {
	private String title ;
	@ItemType(ApprovalLogAndFlowOfRequestDTO.class)
	private List<ApprovalLogAndFlowOfRequestDTO> approvalLogAndFlowOfRequestList;

	public ListApprovalLogAndFlowOfRequestResponse(
			List<ApprovalLogAndFlowOfRequestDTO> approvalLogAndFlowOfRequestList) {
		super();
		this.approvalLogAndFlowOfRequestList = approvalLogAndFlowOfRequestList;
	}

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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
