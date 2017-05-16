package com.everhomes.general_approval;

import com.everhomes.rest.general_form.GeneralFormIdCommand;
import com.everhomes.rest.general_approval.CreateGeneralApprovalCommand;
import com.everhomes.rest.general_approval.GeneralApprovalDTO;
import com.everhomes.rest.general_approval.GeneralApprovalIdCommand;
import com.everhomes.rest.general_approval.GetTemplateByApprovalIdCommand;
import com.everhomes.rest.general_approval.GetTemplateByApprovalIdResponse;
import com.everhomes.rest.general_approval.ListGeneralApprovalCommand;
import com.everhomes.rest.general_approval.ListGeneralApprovalResponse;
import com.everhomes.rest.general_approval.PostApprovalFormCommand;
import com.everhomes.rest.general_approval.UpdateGeneralApprovalCommand;

public interface GeneralApprovalService {

	GetTemplateByApprovalIdResponse getTemplateByApprovalId(GetTemplateByApprovalIdCommand cmd);

	GetTemplateByApprovalIdResponse postApprovalForm(PostApprovalFormCommand cmd);

	void deleteApprovalFormById(GeneralFormIdCommand cmd);

	GeneralApprovalDTO createGeneralApproval(CreateGeneralApprovalCommand cmd);

	ListGeneralApprovalResponse listGeneralApproval(ListGeneralApprovalCommand cmd);

	GeneralApprovalDTO updateGeneralApproval(UpdateGeneralApprovalCommand cmd);

	void deleteGeneralApproval(GeneralApprovalIdCommand cmd);

}
