// @formatter:off
package com.everhomes.rest.approval;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>返回值：
 * <li>nextPageAnchor: 下页锚点</li>
 * <li>approvalRuleList: 审批规则列表，参考{@link com.everhomes.rest.approval.ApprovalRuleDTO}</li>
 * </ul>
 */
public class ListApprovalRuleResponse {
	private Long nextPageAnchor;
	@ItemType(ApprovalRuleDTO.class)
	private List<ApprovalRuleDTO> approvalRuleList;

	public ListApprovalRuleResponse(Long nextPageAnchor, List<ApprovalRuleDTO> approvalRuleList) {
		super();
		this.nextPageAnchor = nextPageAnchor;
		this.approvalRuleList = approvalRuleList;
	}

	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}

	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}

	public List<ApprovalRuleDTO> getApprovalRuleList() {
		return approvalRuleList;
	}

	public void setApprovalRuleList(List<ApprovalRuleDTO> approvalRuleList) {
		this.approvalRuleList = approvalRuleList;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
