package com.everhomes.general_approval;

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
import com.everhomes.rest.general_approval.UpdateApprovalFormCommand;
import com.everhomes.rest.general_approval.UpdateGeneralApprovalCommand;

public interface GeneralApprovalService {

	GetTemplateByApprovalIdResponse getTemplateByApprovalId(GetTemplateByApprovalIdCommand cmd);

	GetTemplateByApprovalIdResponse postApprovalForm(PostApprovalFormCommand cmd);

	GeneralFormDTO createApprovalForm(CreateApprovalFormCommand cmd);

	ListGeneralFormResponse listApprovalForms(ListApprovalFormsCommand cmd);

	void deleteApprovalFormById(ApprovalFormIdCommand cmd);

	GeneralApprovalDTO createGeneralApproval(CreateGeneralApprovalCommand cmd);

	ListGeneralApprovalResponse listGeneralApproval(ListGeneralApprovalCommand cmd);

	GeneralApprovalDTO updateGeneralApproval(UpdateGeneralApprovalCommand cmd);

	void deleteGeneralApproval(GeneralApprovalIdCommand cmd);

	GeneralFormDTO updateApprovalForm(UpdateApprovalFormCommand cmd);

	GeneralFormDTO getApprovalForm(ApprovalFormIdCommand cmd);

}
