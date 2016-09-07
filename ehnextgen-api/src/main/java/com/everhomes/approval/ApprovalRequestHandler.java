package com.everhomes.approval;

import com.everhomes.rest.approval.ApprovalRequestDTO;

public interface ApprovalRequestHandler {
	static final String APPROVAL_REQUEST_OBJECT_PREFIX = "ApprovalRequestHandler_";
	
	void preProcess(ApprovalRequestDTO approvalRequestDTO);
	
	void postProcess(ApprovalRequestDTO approvalRequestDTO);
	
}
