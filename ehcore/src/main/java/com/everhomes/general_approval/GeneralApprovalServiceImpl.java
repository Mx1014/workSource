package com.everhomes.general_approval;

import org.springframework.stereotype.Component;

import com.everhomes.rest.general_approval.ApprovalFormIdCommand;
import com.everhomes.rest.general_approval.CreateApprovalFormCommand;
import com.everhomes.rest.general_approval.CreateGeneralApprovalCommand;
import com.everhomes.rest.general_approval.GeneralApprovalDTO;
import com.everhomes.rest.general_approval.GeneralApprovalIdCommand;
import com.everhomes.rest.general_approval.GeneralFormDTO;
import com.everhomes.rest.general_approval.GetTemplateByApprovalIdCommand;
import com.everhomes.rest.general_approval.GetTemplateByApprovalIdResponse;
import com.everhomes.rest.general_approval.ListApprovalFormsCommand;
import com.everhomes.rest.general_approval.ListGeneralApprovalCommand;
import com.everhomes.rest.general_approval.ListGeneralApprovalResponse;
import com.everhomes.rest.general_approval.ListGeneralFormResponse;
import com.everhomes.rest.general_approval.PostApprovalFormCommand;
import com.everhomes.rest.general_approval.UpdateGeneralApprovalCommand;
@Component
public class GeneralApprovalServiceImpl implements GeneralApprovalService {

	@Override
	public GetTemplateByApprovalIdResponse getTemplateByApprovalId(
			GetTemplateByApprovalIdCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GetTemplateByApprovalIdResponse postApprovalForm(PostApprovalFormCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GeneralFormDTO createApprovalForm(CreateApprovalFormCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ListGeneralFormResponse listApprovalForms(ListApprovalFormsCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteApprovalFormById(ApprovalFormIdCommand cmd) {
		// TODO Auto-generated method stub

	}

	@Override
	public GeneralApprovalDTO createGeneralApproval(CreateGeneralApprovalCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ListGeneralApprovalResponse listGeneralApproval(ListGeneralApprovalCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GeneralApprovalDTO updateGeneralApproval(UpdateGeneralApprovalCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GeneralApprovalDTO deleteGeneralApproval(GeneralApprovalIdCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}

}
