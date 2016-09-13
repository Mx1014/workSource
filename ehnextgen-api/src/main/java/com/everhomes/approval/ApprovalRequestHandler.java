package com.everhomes.approval;

import com.everhomes.rest.approval.ApprovalBasicInfoOfRequestDTO;
import com.everhomes.rest.approval.ApprovalRequestDTO;
import com.everhomes.rest.approval.BriefApprovalRequestDTO;

public interface ApprovalRequestHandler {
	static final String APPROVAL_REQUEST_OBJECT_PREFIX = "ApprovalRequestHandler_";
	
	void preProcess(ApprovalRequestDTO approvalRequestDTO);
	
	void postProcess(ApprovalRequestDTO approvalRequestDTO);

	ApprovalBasicInfoOfRequestDTO processApprovalBasicInfoOfRequest(ApprovalRequest approvalRequest);

	BriefApprovalRequestDTO processBriefApprovalRequest(ApprovalRequest approvalRequest);
	
}
