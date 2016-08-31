// @formatter:off
package com.everhomes.approval;

import java.util.List;

public interface ApprovalRuleFlowMapProvider {

	void createApprovalRuleFlowMap(ApprovalRuleFlowMap approvalRuleFlowMap);

	void updateApprovalRuleFlowMap(ApprovalRuleFlowMap approvalRuleFlowMap);

	ApprovalRuleFlowMap findApprovalRuleFlowMapById(Long id);

	List<ApprovalRuleFlowMap> listApprovalRuleFlowMap();

}