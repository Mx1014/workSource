package com.everhomes.rest.approval;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul> 
 * <li>ruleFlowMapList: 审批规则与审批流程关联列表，参考{@link com.everhomes.rest.approval.RuleFlowMap}</li>
 * </ul>
 */
public class GetTargetApprovalRuleResponse {

	@ItemType(RuleFlowMap.class)
	private List<RuleFlowMap> ruleFlowMapList;

	public List<RuleFlowMap> getRuleFlowMapList() {
		return ruleFlowMapList;
	}

	public void setRuleFlowMapList(List<RuleFlowMap> ruleFlowMapList) {
		this.ruleFlowMapList = ruleFlowMapList;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
