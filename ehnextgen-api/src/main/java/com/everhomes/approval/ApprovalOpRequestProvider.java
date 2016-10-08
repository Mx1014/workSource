// @formatter:off
package com.everhomes.approval;

import java.util.List;

public interface ApprovalOpRequestProvider {

	void createApprovalOpRequest(ApprovalOpRequest approvalOpRequest);

	void updateApprovalOpRequest(ApprovalOpRequest approvalOpRequest);

	ApprovalOpRequest findApprovalOpRequestById(Long id);

	List<ApprovalOpRequest> listApprovalOpRequest();

	List<ApprovalOpRequest> listApprovalOpRequestByRequestId(Long requestId);

	void deleteApprovalOpRequestByRequestId(Long requestId);

}