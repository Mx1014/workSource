// @formatter:off
package com.everhomes.approval;

import java.util.List;

public interface ApprovalRuleFlowMapProvider {

	void createApprovalRuleFlowMap(ApprovalRuleFlowMap approvalRuleFlowMap);

	void updateApprovalRuleFlowMap(ApprovalRuleFlowMap approvalRuleFlowMap);

	ApprovalRuleFlowMap findApprovalRuleFlowMapById(Long id);

	List<ApprovalRuleFlowMap> listApprovalRuleFlowMap();

	ApprovalRuleFlowMap findOneApprovalRuleFlowMapByFlowId(Long flowId);

	void deleteRuleFlowMapByRuleId(Long ruleId);

	void updateApprovalRuleFlowMapByRuleId(Long ruleId);

	List<ApprovalRuleFlowMap> listApprovalRuleFlowMapByRuleIds(List<Long> ruleIdList);

	ApprovalRuleFlowMap findConcreteApprovalRuleFlowMap(Long ruleId, Byte approvalType);

	List<ApprovalRuleFlowMap> listRuleFlowMapsByRuleId(Long ruleId);

}