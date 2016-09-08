// @formatter:off
package com.everhomes.approval;

import com.everhomes.rest.approval.ApprovalRequestDTO;
import com.everhomes.rest.approval.ApproveApprovalRequestCommand;
import com.everhomes.rest.approval.CreateApprovalCategoryCommand;
import com.everhomes.rest.approval.CreateApprovalCategoryResponse;
import com.everhomes.rest.approval.CreateApprovalFlowLevelCommand;
import com.everhomes.rest.approval.CreateApprovalFlowLevelResponse;
import com.everhomes.rest.approval.CreateApprovalFlowInfoCommand;
import com.everhomes.rest.approval.CreateApprovalFlowInfoResponse;
import com.everhomes.rest.approval.CreateApprovalRuleCommand;
import com.everhomes.rest.approval.CreateApprovalRuleResponse;
import com.everhomes.rest.approval.DeleteApprovalCategoryCommand;
import com.everhomes.rest.approval.DeleteApprovalFlowCommand;
import com.everhomes.rest.approval.DeleteApprovalRuleCommand;
import com.everhomes.rest.approval.ListApprovalCategoryCommand;
import com.everhomes.rest.approval.ListApprovalCategoryResponse;
import com.everhomes.rest.approval.ListApprovalFlowCommand;
import com.everhomes.rest.approval.ListApprovalFlowResponse;
import com.everhomes.rest.approval.ListApprovalRuleCommand;
import com.everhomes.rest.approval.ListApprovalRuleResponse;
import com.everhomes.rest.approval.ListBriefApprovalFlowCommand;
import com.everhomes.rest.approval.ListBriefApprovalFlowResponse;
import com.everhomes.rest.approval.ListBriefApprovalRuleCommand;
import com.everhomes.rest.approval.ListBriefApprovalRuleResponse;
import com.everhomes.rest.approval.UpdateApprovalCategoryCommand;
import com.everhomes.rest.approval.UpdateApprovalCategoryResponse;
import com.everhomes.rest.approval.UpdateApprovalFlowLevelCommand;
import com.everhomes.rest.approval.UpdateApprovalFlowLevelResponse;
import com.everhomes.rest.approval.UpdateApprovalFlowInfoCommand;
import com.everhomes.rest.approval.UpdateApprovalFlowInfoResponse;
import com.everhomes.rest.approval.UpdateApprovalRuleCommand;
import com.everhomes.rest.approval.UpdateApprovalRuleResponse;

public interface ApprovalService {

	CreateApprovalCategoryResponse createApprovalCategory(CreateApprovalCategoryCommand cmd);

	UpdateApprovalCategoryResponse updateApprovalCategory(UpdateApprovalCategoryCommand cmd);

	ListApprovalCategoryResponse listApprovalCategory(ListApprovalCategoryCommand cmd);

	void deleteApprovalCategory(DeleteApprovalCategoryCommand cmd);

	CreateApprovalFlowInfoResponse createApprovalFlowInfo(CreateApprovalFlowInfoCommand cmd);

	UpdateApprovalFlowInfoResponse updateApprovalFlowInfo(UpdateApprovalFlowInfoCommand cmd);

	CreateApprovalFlowLevelResponse createApprovalFlowLevel(CreateApprovalFlowLevelCommand cmd);

	UpdateApprovalFlowLevelResponse updateApprovalFlowLevel(UpdateApprovalFlowLevelCommand cmd);

	ListApprovalFlowResponse listApprovalFlow(ListApprovalFlowCommand cmd);

	void deleteApprovalFlow(DeleteApprovalFlowCommand cmd);

	CreateApprovalRuleResponse createApprovalRule(CreateApprovalRuleCommand cmd);

	UpdateApprovalRuleResponse updateApprovalRule(UpdateApprovalRuleCommand cmd);

	void deleteApprovalRule(DeleteApprovalRuleCommand cmd);

	ListApprovalRuleResponse listApprovalRule(ListApprovalRuleCommand cmd);

	ListBriefApprovalRuleResponse listBriefApprovalRule(ListBriefApprovalRuleCommand cmd);

	void approveApprovalRequest(ApproveApprovalRequestCommand cmd);

	ApprovalRequestDTO createApprovalRequest(ApprovalRequestDTO approvalRequestDTO);

	ListBriefApprovalFlowResponse listBriefApprovalFlow(ListBriefApprovalFlowCommand cmd);

}
