// @formatter:off
package com.everhomes.approval;

import java.util.List;

import com.everhomes.rest.approval.ApprovalRequestCondition;

public interface ApprovalRequestProvider {

	void createApprovalRequest(ApprovalRequest approvalRequest);

	void updateApprovalRequest(ApprovalRequest approvalRequest);

	ApprovalRequest findApprovalRequestById(Long id);

	List<ApprovalRequest> listApprovalRequest();

	List<ApprovalRequest> listApprovalRequestByCondition(ApprovalRequestCondition condition);

}