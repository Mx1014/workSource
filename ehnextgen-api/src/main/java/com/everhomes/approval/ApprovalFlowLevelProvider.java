// @formatter:off
package com.everhomes.approval;

import java.util.List;

public interface ApprovalFlowLevelProvider {

	void createApprovalFlowLevel(ApprovalFlowLevel approvalFlowLevel);

	void updateApprovalFlowLevel(ApprovalFlowLevel approvalFlowLevel);

	ApprovalFlowLevel findApprovalFlowLevelById(Long id);

	List<ApprovalFlowLevel> listApprovalFlowLevel();

}