package com.everhomes.rest.approval;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>
 * <li>approvalRequest: 申请信息，参考{@link com.everhomes.rest.approval.BriefApprovalRequestDTO}</li>
 * <li>flowCaseUrl: 工作流跳转url</li>
 * </ul>
 */
public class CreateApprovalRequestBySceneResponse {
	private BriefApprovalRequestDTO approvalRequest;
	private String flowCaseUrl;

	public CreateApprovalRequestBySceneResponse(){
		
	}
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
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

	public String getFlowCaseUrl() {
		return flowCaseUrl;
	}

	public void setFlowCaseUrl(String flowCaseUrl) {
		this.flowCaseUrl = flowCaseUrl;
	}
}
