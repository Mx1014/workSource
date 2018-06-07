package com.everhomes.general_approval;

import com.everhomes.rest.general_approval.*;

public interface GeneralApprovalFormHandler {

    String GENERAL_APPROVAL_FORM_PREFIX = "GeneralApprovalFormHandler_";

    PostGeneralFormDTO postApprovalForm(PostApprovalFormCommand cmd);

    GeneralFormDTO getTemplateBySourceId(GetTemplateBySourceIdCommand cmd);

    GeneralFormReminderDTO getGeneralFormReminder(GeneralFormReminderCommand cmd);
}
