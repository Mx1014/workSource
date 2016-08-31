// @formatter:off
package com.everhomes.approval;

import java.util.List;

public interface ApprovalFlowProvider {

	void createApprovalFlow(ApprovalFlow approvalFlow);

	void updateApprovalFlow(ApprovalFlow approvalFlow);

	ApprovalFlow findApprovalFlowById(Long id);

	List<ApprovalFlow> listApprovalFlow();

}