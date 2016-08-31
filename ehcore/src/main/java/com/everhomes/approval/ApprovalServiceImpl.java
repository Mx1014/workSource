// @formatter:off
package com.everhomes.approval;

import org.springframework.stereotype.Component;

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

@Component
public class ApprovalServiceImpl implements ApprovalService {

	@Override
	public CreateAbsentCategoryResponse createAbsentCategory(CreateAbsentCategoryCommand cmd) {
		return null;
	}

	@Override
	public UpdateAbsentCategoryResponse updateAbsentCategory(UpdateAbsentCategoryCommand cmd) {
		return null;
	}

	@Override
	public ListAbsentTypesResponse listAbsentTypes(ListAbsentTypesCommand cmd) {
		return null;
	}

	@Override
	public void deleteAbsentType(DeleteAbsentTypeCommand cmd) {
	}

	@Override
	public CreateApprovalFlowNameResponse createApprovalFlowName(CreateApprovalFlowNameCommand cmd) {
		return null;
	}

	@Override
	public UpdateApprovalFlowNameResponse updateApprovalFlowName(UpdateApprovalFlowNameCommand cmd) {
		return null;
	}

	@Override
	public CreateApprovalFlowLevelResponse createApprovalFlowLevel(CreateApprovalFlowLevelCommand cmd) {
		return null;
	}

	@Override
	public UpdateApprovalFlowLevelResponse updateApprovalFlowLevel(UpdateApprovalFlowLevelCommand cmd) {
		return null;
	}

	@Override
	public ListApprovalFlowsResponse listApprovalFlows(ListApprovalFlowsCommand cmd) {
		return null;
	}

	@Override
	public void deleteApprovalFlow(DeleteApprovalFlowCommand cmd) {
	}

	@Override
	public CreateApprovalRuleResponse createApprovalRule(CreateApprovalRuleCommand cmd) {
		return null;
	}

	@Override
	public UpdateApprovalRuleResponse updateApprovalRule(UpdateApprovalRuleCommand cmd) {
		return null;
	}

	@Override
	public void deleteApprovalRule(DeleteApprovalRuleCommand cmd) {
	}

	@Override
	public ListApprovalRulesResponse listApprovalRules(ListApprovalRulesCommand cmd) {
		return null;
	}

}
