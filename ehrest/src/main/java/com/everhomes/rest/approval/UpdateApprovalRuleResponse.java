// @formatter:off
package com.everhomes.rest.approval;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * 返回值:
 * <li>approvalRule: 审批规则,参考{@link com.everhomes.rest.approval.ApprovalRuleDTO}</li>
 * </ul>
 */
public class UpdateApprovalRuleResponse {
	private ApprovalRuleDTO approvalRule;

	public UpdateApprovalRuleResponse(ApprovalRuleDTO approvalRule) {
		super();
		this.approvalRule = approvalRule;
	}

	public ApprovalRuleDTO getApprovalRule() {
		return approvalRule;
	}

	public void setApprovalRule(ApprovalRuleDTO approvalRule) {
		this.approvalRule = approvalRule;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	
}
