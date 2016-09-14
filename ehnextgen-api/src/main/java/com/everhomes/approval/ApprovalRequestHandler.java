package com.everhomes.approval;

import com.everhomes.rest.approval.ApprovalBasicInfoOfRequestDTO;
import com.everhomes.rest.approval.ApprovalOwnerInfo;
import com.everhomes.rest.approval.BriefApprovalRequestDTO;
import com.everhomes.rest.approval.CreateApprovalRequestBySceneCommand;

public interface ApprovalRequestHandler {
	static final String APPROVAL_REQUEST_OBJECT_PREFIX = "ApprovalRequestHandler_";

	ApprovalBasicInfoOfRequestDTO processApprovalBasicInfoOfRequest(ApprovalRequest approvalRequest);

	BriefApprovalRequestDTO processBriefApprovalRequest(ApprovalRequest approvalRequest);

	ApprovalRequest preProcessCreateApprovalRequest(Long userId, ApprovalOwnerInfo ownerInfo, CreateApprovalRequestBySceneCommand cmd);

	void postProcessCreateApprovalRequest(Long userId, ApprovalOwnerInfo ownerInfo, ApprovalRequest approvalRequest, CreateApprovalRequestBySceneCommand cmd);
	
}
