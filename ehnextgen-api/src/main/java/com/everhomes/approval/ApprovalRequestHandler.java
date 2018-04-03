package com.everhomes.approval;

import java.util.List;

import com.everhomes.rest.approval.ApprovalBasicInfoOfRequestDTO;
import com.everhomes.rest.approval.ApprovalOwnerInfo;
import com.everhomes.rest.approval.BriefApprovalRequestDTO;
import com.everhomes.rest.approval.CreateApprovalRequestBySceneCommand;
import com.everhomes.rest.approval.ListApprovalLogAndFlowOfRequestBySceneResponse;
import com.everhomes.rest.approval.RequestDTO;
import com.everhomes.rest.flow.FlowCaseEntity;

public interface ApprovalRequestHandler {
	static final String APPROVAL_REQUEST_OBJECT_PREFIX = "ApprovalRequestHandler_";

	ApprovalBasicInfoOfRequestDTO processApprovalBasicInfoOfRequest(ApprovalRequest approvalRequest);

	BriefApprovalRequestDTO processBriefApprovalRequest(ApprovalRequest approvalRequest);

	ApprovalRequest preProcessCreateApprovalRequest(Long userId, ApprovalOwnerInfo ownerInfo, CreateApprovalRequestBySceneCommand cmd);

	String postProcessCreateApprovalRequest(Long userId, ApprovalOwnerInfo ownerInfo, ApprovalRequest approvalRequest, CreateApprovalRequestBySceneCommand cmd);

	void processCancelApprovalRequest(ApprovalRequest approvalRequest);

	void processFinalApprove(ApprovalRequest approvalRequest);

	List<RequestDTO> processListApprovalRequest(List<ApprovalRequest> approvalRequestList);

	String processMessageToCreatorBody(ApprovalRequest approvalRequest, String reason);

	String processMessageToNextLevelBody(ApprovalRequest approvalRequest);

	String ApprovalLogAndFlowOfRequestResponseTitle(
			ApprovalRequest approvalRequest);

	ListApprovalLogAndFlowOfRequestBySceneResponse processListApprovalLogAndFlowOfRequestBySceneResponse(
			ListApprovalLogAndFlowOfRequestBySceneResponse result,
			ApprovalRequest approvalRequest);

	BriefApprovalRequestDTO processApprovalRequestByScene(
			ApprovalRequest approvalRequest);

	void calculateRangeStat(ApprovalRequest approvalRequest);

	List<FlowCaseEntity> getFlowCaseEntities(ApprovalRequest approvalRequest);
	
}
