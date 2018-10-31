package com.everhomes.general_approval;

import com.everhomes.rest.general_approval.CreateOrUpdateGeneralFormValuesWithFlowCommand;
import com.everhomes.rest.general_approval.GeneralFormDTO;
import com.everhomes.rest.general_approval.GeneralFormReminderCommand;
import com.everhomes.rest.general_approval.GeneralFormReminderDTO;
import com.everhomes.rest.general_approval.GetGeneralFormsAndValuesByFlowNodeCommand;
import com.everhomes.rest.general_approval.GetTemplateBySourceIdCommand;
import com.everhomes.rest.general_approval.ListGeneralFormResponse;
import com.everhomes.rest.general_approval.PostApprovalFormCommand;
import com.everhomes.rest.general_approval.PostGeneralFormDTO;

public interface GeneralApprovalFormHandler {

    String GENERAL_APPROVAL_FORM_PREFIX = "GeneralApprovalFormHandler_";

    PostGeneralFormDTO postApprovalForm(PostApprovalFormCommand cmd);

    GeneralFormDTO getTemplateBySourceId(GetTemplateBySourceIdCommand cmd);

    GeneralFormReminderDTO getGeneralFormReminder(GeneralFormReminderCommand cmd);
}
