package com.everhomes.general_approval;

import com.everhomes.rest.general_approval.*;

public interface GeneralApprovalService {

	GetTemplateByApprovalIdResponse getTemplateByApprovalId(GetTemplateByApprovalIdCommand cmd);

	GetTemplateByApprovalIdResponse postApprovalForm(PostApprovalFormCommand cmd);

	void deleteApprovalFormById(GeneralFormIdCommand cmd);

	GeneralApprovalDTO createGeneralApproval(CreateGeneralApprovalCommand cmd);

	ListGeneralApprovalResponse listGeneralApproval(ListGeneralApprovalCommand cmd);

	GeneralApprovalDTO updateGeneralApproval(UpdateGeneralApprovalCommand cmd);

	void deleteGeneralApproval(GeneralApprovalIdCommand cmd);

}
