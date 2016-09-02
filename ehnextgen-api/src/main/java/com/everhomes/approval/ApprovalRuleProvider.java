// @formatter:off
package com.everhomes.approval;

import java.util.List;

public interface ApprovalRuleProvider {

	void createApprovalRule(ApprovalRule approvalRule);

	void updateApprovalRule(ApprovalRule approvalRule);

	ApprovalRule findApprovalRuleById(Long id);

	List<ApprovalRule> listApprovalRule();

	List<ApprovalRule> listApprovalRule(Integer namespaceId, String ownerType, Long ownerId, Long pageAnchor, int pageSize);

	ApprovalRule findApprovalRuleByName(Integer namespaceId, String ownerType, Long ownerId, String ruleName);

}