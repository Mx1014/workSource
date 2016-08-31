// @formatter:off
package com.everhomes.approval;

import com.everhomes.rest.approval.CreateAbsentCategoryCommand;
import com.everhomes.rest.approval.CreateAbsentCategoryResponse;
import com.everhomes.rest.approval.CreateApprovalFlowLevelCommand;
import com.everhomes.rest.approval.CreateApprovalFlowLevelResponse;
import com.everhomes.rest.approval.CreateApprovalFlowNameCommand;
import com.everhomes.rest.approval.CreateApprovalFlowNameResponse;
import com.everhomes.rest.approval.CreateApprovalRuleCommand;
import com.everhomes.rest.approval.CreateApprovalRuleResponse;
import com.everhomes.rest.approval.DeleteAbsentTypeCommand;
import com.everhomes.rest.approval.DeleteApprovalFlowCommand;
import com.everhomes.rest.approval.DeleteApprovalRuleCommand;
import com.everhomes.rest.approval.ListAbsentTypesCommand;
import com.everhomes.rest.approval.ListAbsentTypesResponse;
import com.everhomes.rest.approval.ListApprovalFlowsCommand;
import com.everhomes.rest.approval.ListApprovalFlowsResponse;
import com.everhomes.rest.approval.ListApprovalRulesCommand;
import com.everhomes.rest.approval.ListApprovalRulesResponse;
import com.everhomes.rest.approval.UpdateAbsentCategoryCommand;
import com.everhomes.rest.approval.UpdateAbsentCategoryResponse;
import com.everhomes.rest.approval.UpdateApprovalFlowLevelCommand;
import com.everhomes.rest.approval.UpdateApprovalFlowLevelResponse;
import com.everhomes.rest.approval.UpdateApprovalFlowNameCommand;
import com.everhomes.rest.approval.UpdateApprovalFlowNameResponse;
import com.everhomes.rest.approval.UpdateApprovalRuleCommand;
import com.everhomes.rest.approval.UpdateApprovalRuleResponse;

public interface ApprovalService {

	CreateAbsentCategoryResponse createAbsentCategory(CreateAbsentCategoryCommand cmd);

	UpdateAbsentCategoryResponse updateAbsentCategory(UpdateAbsentCategoryCommand cmd);

	ListAbsentTypesResponse listAbsentTypes(ListAbsentTypesCommand cmd);

	void deleteAbsentType(DeleteAbsentTypeCommand cmd);

	CreateApprovalFlowNameResponse createApprovalFlowName(CreateApprovalFlowNameCommand cmd);

	UpdateApprovalFlowNameResponse updateApprovalFlowName(UpdateApprovalFlowNameCommand cmd);

	CreateApprovalFlowLevelResponse createApprovalFlowLevel(CreateApprovalFlowLevelCommand cmd);

	UpdateApprovalFlowLevelResponse updateApprovalFlowLevel(UpdateApprovalFlowLevelCommand cmd);

	ListApprovalFlowsResponse listApprovalFlows(ListApprovalFlowsCommand cmd);

	void deleteApprovalFlow(DeleteApprovalFlowCommand cmd);

	CreateApprovalRuleResponse createApprovalRule(CreateApprovalRuleCommand cmd);

	UpdateApprovalRuleResponse updateApprovalRule(UpdateApprovalRuleCommand cmd);

	void deleteApprovalRule(DeleteApprovalRuleCommand cmd);

	ListApprovalRulesResponse listApprovalRules(ListApprovalRulesCommand cmd);

}
