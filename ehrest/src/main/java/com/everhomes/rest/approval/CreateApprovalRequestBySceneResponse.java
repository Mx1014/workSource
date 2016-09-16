package com.everhomes.rest.approval;

/**
 * 
 * <ul>
 * <li>approvalRequest: 申请信息，参考{@link com.everhomes.rest.approval.BriefApprovalRequestDTO}</li>
 * </ul>
 */
public class CreateApprovalRequestBySceneResponse {
	private BriefApprovalRequestDTO approvalRequest;

	public CreateApprovalRequestBySceneResponse(BriefApprovalRequestDTO approvalRequest) {
		super();
		this.approvalRequest = approvalRequest;
	}

	public BriefApprovalRequestDTO getApprovalRequest() {
		return approvalRequest;
	}

	public void setApprovalRequest(BriefApprovalRequestDTO approvalRequest) {
		this.approvalRequest = approvalRequest;
	}
	
	
}
