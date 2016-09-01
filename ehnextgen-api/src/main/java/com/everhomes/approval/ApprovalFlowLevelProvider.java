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

//	void createApprovalFlowLevelList(List<ApprovalFlowLevel> approvalFlowLevelList);

}