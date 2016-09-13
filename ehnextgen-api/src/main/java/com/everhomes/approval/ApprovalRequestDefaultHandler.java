package com.everhomes.approval;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.rest.approval.ApprovalBasicInfoOfRequestDTO;
import com.everhomes.rest.approval.ApprovalRequestDTO;
import com.everhomes.rest.approval.BriefApprovalRequestDTO;
import com.everhomes.rest.approval.TrueOrFalseFlag;
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
	public void preProcess(ApprovalRequestDTO approvalRequestDTO) {
	}

	@Override
	public void postProcess(ApprovalRequestDTO approvalRequestDTO) {
	}

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
		if (approvalRequest.getTimeFlag().byteValue() == TrueOrFalseFlag.TRUE.getCode()) {
			briefApprovalRequestDTO.setTimeRangeList(approvalService.listTimeRangeByRequestId(approvalRequest.getId()));
		}
		if (approvalRequest.getAttachmentFlag().byteValue() == TrueOrFalseFlag.TRUE.getCode()) {
			briefApprovalRequestDTO.setAttachmentList(approvalService.listAttachmentByRequestId(approvalRequest.getId()));
		}
		
		return briefApprovalRequestDTO;
	}
	
}
