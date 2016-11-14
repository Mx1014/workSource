// @formatter:off
package com.everhomes.approval;

import java.util.List;

public interface ApprovalFlowProvider {

	void createApprovalFlow(ApprovalFlow approvalFlow);

	void updateApprovalFlow(ApprovalFlow approvalFlow);

	ApprovalFlow findApprovalFlowById(Long id);

	List<ApprovalFlow> listApprovalFlow();

	List<ApprovalFlow> listApprovalFlow(Integer namespaceId, String ownerType, Long ownerId, Long pageAnchor, int pageSize);

	ApprovalFlow findApprovalFlowByName(Integer namespaceId, String ownerType, Long ownerId, String name);

	List<ApprovalFlow> listApprovalFlow(Integer namespaceId, String ownerType, Long ownerId);
 

	void deleteApprovalFlows(List<Long> flowIds);

}