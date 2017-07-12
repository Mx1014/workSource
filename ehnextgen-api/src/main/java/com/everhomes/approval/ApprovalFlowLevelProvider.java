// @formatter:off
package com.everhomes.approval;

import java.util.List;

public interface ApprovalFlowLevelProvider {

	void createApprovalFlowLevel(ApprovalFlowLevel approvalFlowLevel);

	void updateApprovalFlowLevel(ApprovalFlowLevel approvalFlowLevel);

	ApprovalFlowLevel findApprovalFlowLevelById(Long id);

	List<ApprovalFlowLevel> listApprovalFlowLevel();

	List<ApprovalFlowLevel> listApprovalFlowLevel(Long flowId, Byte level);

	void deleteApprovalLevels(Long flowId, Byte level);

	List<ApprovalFlowLevel> listApprovalFlowLevelByFlowIds(List<Long> flowIdList);

	List<ApprovalFlowLevel> listApprovalFlowLevelByFlowId(Long flowId);

	ApprovalFlowLevel findApprovalFlowLevel(Byte targetType, Long targetId, Long flowId, Byte level);

	List<ApprovalFlowLevel> listApprovalFlowLevelByTarget(Integer namespaceId, String ownerType, Long ownerId, byte targetType, Long targetId);

	void deleteApprovalLevels(List<Long> flowIds);


//	void createApprovalFlowLevelList(List<ApprovalFlowLevel> approvalFlowLevelList);

}