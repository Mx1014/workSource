package com.everhomes.approval;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.rest.approval.ApprovalBasicInfoOfRequestDTO;
import com.everhomes.rest.approval.ApprovalOwnerInfo;
import com.everhomes.rest.approval.ApprovalStatus;
import com.everhomes.rest.approval.BriefApprovalRequestDTO;
import com.everhomes.rest.approval.CommonStatus;
import com.everhomes.rest.approval.CreateApprovalRequestBySceneCommand;
import com.everhomes.rest.approval.TrueOrFalseFlag;
import com.everhomes.util.DateHelper;
import com.everhomes.util.ListUtils;
import com.everhomes.util.WebTokenGenerator;

/**
 * 
 * <ul>
 * 用于审批的默认handler
 * </ul>
 */
@Component(ApprovalRequestDefaultHandler.APPROVAL_REQUEST_DEFAULT_HANDLER_NAME)
public class ApprovalRequestDefaultHandler implements ApprovalRequestHandler {
	
	static final String APPROVAL_REQUEST_DEFAULT_HANDLER_NAME = ApprovalRequestHandler.APPROVAL_REQUEST_OBJECT_PREFIX + "Default";
	
	@Autowired
	private ApprovalCategoryProvider approvalCategoryProvider;
	
	@Autowired
	private ApprovalService approvalService;

	@Override
	public ApprovalBasicInfoOfRequestDTO processApprovalBasicInfoOfRequest(ApprovalRequest approvalRequest) {
		return new ApprovalBasicInfoOfRequestDTO(approvalRequest.getApprovalType(), approvalRequest.getApprovalStatus());
	}

	@Override
	public BriefApprovalRequestDTO processBriefApprovalRequest(ApprovalRequest approvalRequest) {
		BriefApprovalRequestDTO briefApprovalRequestDTO = new BriefApprovalRequestDTO();
		briefApprovalRequestDTO.setRequestToken(WebTokenGenerator.getInstance().toWebToken(approvalRequest.getId()));
		briefApprovalRequestDTO.setApprovalType(approvalRequest.getApprovalType());
		if (approvalRequest.getCategoryId() != null) {
			ApprovalCategory approvalCategory = approvalCategoryProvider.findApprovalCategoryById(approvalRequest.getCategoryId());
			if (approvalCategory != null) {
				briefApprovalRequestDTO.setCategoryName(approvalCategory.getCategoryName());
			}
		}
		briefApprovalRequestDTO.setApprovalStatus(approvalRequest.getApprovalStatus());
		briefApprovalRequestDTO.setCreateTime(approvalRequest.getCreateTime());
		briefApprovalRequestDTO.setReason(approvalRequest.getReason());
		if (approvalRequest.getTimeFlag().byteValue() == TrueOrFalseFlag.TRUE.getCode()) {
			briefApprovalRequestDTO.setTimeRangeList(approvalService.listTimeRangeByRequestId(approvalRequest.getId()));
		}
		if (approvalRequest.getAttachmentFlag().byteValue() == TrueOrFalseFlag.TRUE.getCode()) {
			briefApprovalRequestDTO.setAttachmentList(approvalService.listAttachmentByRequestId(approvalRequest.getId()));
		}
		
		return briefApprovalRequestDTO;
	}

	@Override
	public ApprovalRequest preProcessCreateApprovalRequest(Long userId, ApprovalOwnerInfo ownerInfo,
			CreateApprovalRequestBySceneCommand cmd) {
		return generateApprovalRequest(userId, ownerInfo, cmd);
	}

	@Override
	public void postProcessCreateApprovalRequest(Long userId, ApprovalOwnerInfo ownerInfo, ApprovalRequest approvalRequest,
			CreateApprovalRequestBySceneCommand cmd) {
	}
	

	private ApprovalRequest generateApprovalRequest(Long userId, ApprovalOwnerInfo ownerInfo, CreateApprovalRequestBySceneCommand cmd) {
		ApprovalRequest approvalRequest = new ApprovalRequest();
		approvalRequest.setNamespaceId(ownerInfo.getNamespaceId());
		approvalRequest.setOwnerType(ownerInfo.getOwnerType());
		approvalRequest.setOwnerId(ownerInfo.getOwnerId());
		approvalRequest.setApprovalType(cmd.getApprovalType());
		approvalRequest.setCategoryId(cmd.getCategoryId());
		approvalRequest.setReason(cmd.getReason());
		approvalRequest.setAttachmentFlag(ListUtils.isEmpty(cmd.getAttachmentList())?TrueOrFalseFlag.FALSE.getCode():TrueOrFalseFlag.TRUE.getCode());
		approvalRequest.setTimeFlag(ListUtils.isEmpty(cmd.getTimeRangeList())?TrueOrFalseFlag.FALSE.getCode():TrueOrFalseFlag.TRUE.getCode());
		approvalRequest.setApprovalStatus(ApprovalStatus.WAITING_FOR_APPROVING.getCode());
		approvalRequest.setStatus(CommonStatus.ACTIVE.getCode());
		approvalRequest.setCreatorUid(userId);
		approvalRequest.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		approvalRequest.setUpdateTime(approvalRequest.getCreateTime());
		approvalRequest.setOperatorUid(userId);
		approvalRequest.setFlowId(approvalService.getApprovalFlowByUser(ownerInfo.getOwnerType(), ownerInfo.getOwnerId(), userId, cmd.getApprovalType()).getId());
		approvalRequest.setCurrentLevel((byte) 0);
		approvalRequest.setNextLevel((byte) 1);
		return approvalRequest;
	}

	@Override
	public void processCancelApprovalRequest(ApprovalRequest approvalRequest) {
		
	}

	@Override
	public void processFinalApprove(ApprovalRequest approvalRequest) {
	}

	@Override
	public String processListApprovalRequest(List<ApprovalRequest> approvalRequestList) {
		return null;
	}
	
	
}
