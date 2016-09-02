// @formatter:off
package com.everhomes.rest.approval;

import java.util.List;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>返回值
 * <li>approvalRuleList: 简短的审批规则列表</li>
 * </ul>
 */
public class ListBriefApprovalRuleResponse {
	private List<BriefApprovalRuleDTO> approvalRuleList;
	
	public List<BriefApprovalRuleDTO> getApprovalRuleList() {
		return approvalRuleList;
	}

	public void setApprovalRuleList(List<BriefApprovalRuleDTO> approvalRuleList) {
		this.approvalRuleList = approvalRuleList;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}	
