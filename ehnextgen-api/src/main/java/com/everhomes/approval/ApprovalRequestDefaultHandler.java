package com.everhomes.approval;

import org.springframework.stereotype.Component;

import com.everhomes.rest.approval.ApprovalRequestDTO;

@Component(ApprovalRequestDefaultHandler.APPROVAL_REQUEST_DEFAULT_HANDLER_NAME)
public class ApprovalRequestDefaultHandler implements ApprovalRequestHandler {
	
	static final String APPROVAL_REQUEST_DEFAULT_HANDLER_NAME = ApprovalRequestHandler.APPROVAL_REQUEST_OBJECT_PREFIX + "Default";
	
	@Override
	public void preProcess(ApprovalRequestDTO approvalRequestDTO) {
	}

	@Override
	public void postProcess(ApprovalRequestDTO approvalRequestDTO) {
	}
	
}
