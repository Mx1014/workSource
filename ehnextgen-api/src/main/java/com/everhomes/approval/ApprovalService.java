// @formatter:off
package com.everhomes.approval;

import java.util.List;

import com.everhomes.rest.approval.ApproveApprovalRequesBySceneCommand;
import com.everhomes.rest.approval.ApproveApprovalRequestCommand;
import com.everhomes.rest.approval.CancelApprovalRequestBySceneCommand;
import com.everhomes.rest.approval.CreateApprovalCategoryCommand;
import com.everhomes.rest.approval.CreateApprovalCategoryResponse;
import com.everhomes.rest.approval.CreateApprovalFlowInfoCommand;
import com.everhomes.rest.approval.CreateApprovalFlowInfoResponse;
import com.everhomes.rest.approval.CreateApprovalFlowLevelCommand;
import com.everhomes.rest.approval.CreateApprovalFlowLevelResponse;
import com.everhomes.rest.approval.CreateApprovalRequestBySceneCommand;
import com.everhomes.rest.approval.CreateApprovalRequestBySceneResponse;
import com.everhomes.rest.approval.CreateApprovalRuleCommand;
import com.everhomes.rest.approval.CreateApprovalRuleResponse;
import com.everhomes.rest.approval.DeleteApprovalCategoryCommand;
import com.everhomes.rest.approval.DeleteApprovalFlowCommand;
import com.everhomes.rest.approval.DeleteApprovalRuleCommand;
import com.everhomes.rest.approval.GetApprovalBasicInfoOfRequestBySceneCommand;
import com.everhomes.rest.approval.GetApprovalBasicInfoOfRequestBySceneResponse;
import com.everhomes.rest.approval.GetApprovalBasicInfoOfRequestCommand;
import com.everhomes.rest.approval.GetApprovalBasicInfoOfRequestResponse;
import com.everhomes.rest.approval.GetTargetApprovalRuleCommand;
import com.everhomes.rest.approval.GetTargetApprovalRuleResponse;
import com.everhomes.rest.approval.ListApprovalCategoryBySceneCommand;
import com.everhomes.rest.approval.ListApprovalCategoryBySceneResponse;
import com.everhomes.rest.approval.ListApprovalCategoryCommand;
import com.everhomes.rest.approval.ListApprovalCategoryResponse;
import com.everhomes.rest.approval.ListApprovalFlowCommand;
import com.everhomes.rest.approval.ListApprovalFlowOfRequestBySceneCommand;
import com.everhomes.rest.approval.ListApprovalFlowOfRequestBySceneResponse;
import com.everhomes.rest.approval.ListApprovalFlowOfRequestCommand;
import com.everhomes.rest.approval.ListApprovalFlowOfRequestResponse;
import com.everhomes.rest.approval.ListApprovalFlowResponse;
import com.everhomes.rest.approval.ListApprovalLogAndFlowOfRequestBySceneCommand;
import com.everhomes.rest.approval.ListApprovalLogAndFlowOfRequestBySceneResponse;
import com.everhomes.rest.approval.ListApprovalLogAndFlowOfRequestCommand;
import com.everhomes.rest.approval.ListApprovalLogAndFlowOfRequestResponse;
import com.everhomes.rest.approval.ListApprovalLogOfRequestBySceneCommand;
import com.everhomes.rest.approval.ListApprovalLogOfRequestBySceneResponse;
import com.everhomes.rest.approval.ListApprovalLogOfRequestCommand;
import com.everhomes.rest.approval.ListApprovalLogOfRequestResponse;
import com.everhomes.rest.approval.ListApprovalRequestBySceneCommand;
import com.everhomes.rest.approval.ListApprovalRequestBySceneResponse;
import com.everhomes.rest.approval.ListApprovalRequestCommand;
import com.everhomes.rest.approval.ListApprovalRequestResponse;
import com.everhomes.rest.approval.ListApprovalRuleCommand;
import com.everhomes.rest.approval.ListApprovalRuleResponse;
import com.everhomes.rest.approval.ListApprovalUserCommand;
import com.everhomes.rest.approval.ListApprovalUserResponse;
import com.everhomes.rest.approval.ListBriefApprovalFlowCommand;
import com.everhomes.rest.approval.ListBriefApprovalFlowResponse;
import com.everhomes.rest.approval.ListBriefApprovalRuleCommand;
import com.everhomes.rest.approval.ListBriefApprovalRuleResponse;
import com.everhomes.rest.approval.ListMyApprovalsBySceneCommand;
import com.everhomes.rest.approval.ListTargetUsersCommand;
import com.everhomes.rest.approval.RejectApprovalRequestBySceneCommand;
import com.everhomes.rest.approval.RejectApprovalRequestCommand;
import com.everhomes.rest.approval.TimeRange;
import com.everhomes.rest.approval.UpdateApprovalCategoryCommand;
import com.everhomes.rest.approval.UpdateApprovalCategoryResponse;
import com.everhomes.rest.approval.UpdateApprovalFlowInfoCommand;
import com.everhomes.rest.approval.UpdateApprovalFlowInfoResponse;
import com.everhomes.rest.approval.UpdateApprovalFlowLevelCommand;
import com.everhomes.rest.approval.UpdateApprovalFlowLevelResponse;
import com.everhomes.rest.approval.UpdateApprovalRuleCommand;
import com.everhomes.rest.approval.UpdateApprovalRuleResponse;
import com.everhomes.rest.approval.UpdateTargetApprovalRuleCommand;
import com.everhomes.rest.news.AttachmentDescriptor;
import com.everhomes.rest.organization.OrganizationMemberDTO;

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

	ListBriefApprovalFlowResponse listBriefApprovalFlow(ListBriefApprovalFlowCommand cmd);

	void rejectApprovalRequest(RejectApprovalRequestCommand cmd);

	GetApprovalBasicInfoOfRequestResponse getApprovalBasicInfoOfRequest(GetApprovalBasicInfoOfRequestCommand cmd);

	ListApprovalLogAndFlowOfRequestResponse listApprovalLogAndFlowOfRequest(ListApprovalLogAndFlowOfRequestCommand cmd);

	ListApprovalLogOfRequestResponse listApprovalLogOfRequest(ListApprovalLogOfRequestCommand cmd);

	ListApprovalFlowOfRequestResponse listApprovalFlowOfRequest(ListApprovalFlowOfRequestCommand cmd);

	ListApprovalUserResponse listApprovalUser(ListApprovalUserCommand cmd);

	ListApprovalRequestBySceneResponse listApprovalRequestByScene(ListApprovalRequestBySceneCommand cmd);

	GetApprovalBasicInfoOfRequestBySceneResponse getApprovalBasicInfoOfRequestByScene(GetApprovalBasicInfoOfRequestBySceneCommand cmd);

	ListApprovalLogAndFlowOfRequestBySceneResponse listApprovalLogAndFlowOfRequestByScene(ListApprovalLogAndFlowOfRequestBySceneCommand cmd);

	ListApprovalLogOfRequestBySceneResponse listApprovalLogOfRequestByScene(ListApprovalLogOfRequestBySceneCommand cmd);

	ListApprovalFlowOfRequestBySceneResponse listApprovalFlowOfRequestByScene(ListApprovalFlowOfRequestBySceneCommand cmd);

	List<TimeRange> listTimeRangeByRequestId(Long requestId);

	List<AttachmentDescriptor> listAttachmentByRequestId(Long requestId);

	CreateApprovalRequestBySceneResponse createApprovalRequestByScene(CreateApprovalRequestBySceneCommand cmd);

	ListApprovalRequestResponse listApprovalRequest(ListApprovalRequestCommand cmd);

	ApprovalFlow getApprovalFlowByUser(String ownerType, Long ownerId, Long userId, Byte approvalType);

	ListApprovalCategoryBySceneResponse listApprovalCategoryByScene(ListApprovalCategoryBySceneCommand cmd);

	void cancelApprovalRequestByScene(CancelApprovalRequestBySceneCommand cmd);

	String getUserName(Long userId, Long organizationId);

	GetTargetApprovalRuleResponse getTargetApprovalRule(GetTargetApprovalRuleCommand cmd);

	void updateTargetApprovalRule(UpdateTargetApprovalRuleCommand cmd);

	void deleteTargetApprovalRule(GetTargetApprovalRuleCommand cmd);

	ListApprovalRequestBySceneResponse listMyApprovalsByScene(ListMyApprovalsBySceneCommand cmd);

	void approveApprovalRequest(ApproveApprovalRequesBySceneCommand cmd);

	void rejectApprovalRequest(RejectApprovalRequestBySceneCommand cmd);

	ApprovalCategory findApprovalCategoryById(Long id);

	List<OrganizationMemberDTO> listTargetUsers(ListTargetUsersCommand cmd);

	ApprovalRequestHandler getApprovalRequestHandler(Byte approvalType);

	void finishApproveApprovalRequest(ApprovalRequest approvalRequest);

}
