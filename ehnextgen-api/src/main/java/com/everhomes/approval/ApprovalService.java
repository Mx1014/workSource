// @formatter:off
package com.everhomes.approval;

import com.everhomes.rest.approval.CreateAskForLeaveCategoryCommand;
import com.everhomes.rest.approval.CreateAskForLeaveCategoryResponse;
import com.everhomes.rest.approval.CreateApprovalFlowLevelCommand;
import com.everhomes.rest.approval.CreateApprovalFlowLevelResponse;
import com.everhomes.rest.approval.CreateApprovalFlowNameCommand;
import com.everhomes.rest.approval.CreateApprovalFlowNameResponse;
import com.everhomes.rest.approval.CreateApprovalRuleCommand;
import com.everhomes.rest.approval.CreateApprovalRuleResponse;
import com.everhomes.rest.approval.DeleteAskForLeaveCategoryCommand;
import com.everhomes.rest.approval.DeleteApprovalFlowCommand;
import com.everhomes.rest.approval.DeleteApprovalRuleCommand;
import com.everhomes.rest.approval.ListAskForLeaveCategoryCommand;
import com.everhomes.rest.approval.ListAskForLeaveCategoryResponse;
import com.everhomes.rest.approval.ListApprovalFlowCommand;
import com.everhomes.rest.approval.ListApprovalFlowResponse;
import com.everhomes.rest.approval.ListApprovalRuleCommand;
import com.everhomes.rest.approval.ListApprovalRuleResponse;
import com.everhomes.rest.approval.UpdateAskForLeaveCategoryCommand;
import com.everhomes.rest.approval.UpdateAskForLeaveCategoryResponse;
import com.everhomes.rest.approval.UpdateApprovalFlowLevelCommand;
import com.everhomes.rest.approval.UpdateApprovalFlowLevelResponse;
import com.everhomes.rest.approval.UpdateApprovalFlowNameCommand;
import com.everhomes.rest.approval.UpdateApprovalFlowNameResponse;
import com.everhomes.rest.approval.UpdateApprovalRuleCommand;
import com.everhomes.rest.approval.UpdateApprovalRuleResponse;

public interface ApprovalService {

	CreateAskForLeaveCategoryResponse createAskForLeaveCategory(CreateAskForLeaveCategoryCommand cmd);

	UpdateAskForLeaveCategoryResponse updateAskForLeaveCategory(UpdateAskForLeaveCategoryCommand cmd);

	ListAskForLeaveCategoryResponse listAskForLeaveCategory(ListAskForLeaveCategoryCommand cmd);

	void deleteAskForLeaveCategory(DeleteAskForLeaveCategoryCommand cmd);

	CreateApprovalFlowNameResponse createApprovalFlowName(CreateApprovalFlowNameCommand cmd);

	UpdateApprovalFlowNameResponse updateApprovalFlowName(UpdateApprovalFlowNameCommand cmd);

	CreateApprovalFlowLevelResponse createApprovalFlowLevel(CreateApprovalFlowLevelCommand cmd);

	UpdateApprovalFlowLevelResponse updateApprovalFlowLevel(UpdateApprovalFlowLevelCommand cmd);

	ListApprovalFlowResponse listApprovalFlow(ListApprovalFlowCommand cmd);

	void deleteApprovalFlow(DeleteApprovalFlowCommand cmd);

	CreateApprovalRuleResponse createApprovalRule(CreateApprovalRuleCommand cmd);

	UpdateApprovalRuleResponse updateApprovalRule(UpdateApprovalRuleCommand cmd);

	void deleteApprovalRule(DeleteApprovalRuleCommand cmd);

	ListApprovalRuleResponse listApprovalRule(ListApprovalRuleCommand cmd);

}
