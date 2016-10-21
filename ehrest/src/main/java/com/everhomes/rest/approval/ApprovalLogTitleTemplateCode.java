package com.everhomes.rest.approval;

public interface ApprovalLogTitleTemplateCode {
	String SCOPE = "approval.title";
	
	int WAITING_FOR_APPROVING = 1; // ${nickName}提交了${approvalType}申请
	int APPROVING_FLOW = 2; // 等待${nickNames}审批
	 
	
}
