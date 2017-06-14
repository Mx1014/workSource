package com.everhomes.general_form;

import com.everhomes.rest.general_approval.GeneralFormDTO;
import com.everhomes.rest.general_approval.GetTemplateBySourceIdCommand;
import com.everhomes.rest.general_approval.PostGeneralFormCommand;
import com.everhomes.rest.general_approval.PostGeneralFormDTO;

/**
 * <p></p>
 */
public interface GeneralFormModuleHandler {

    String GENERAL_FORM_MODULE_HANDLER_PREFIX = "GeneralFormModuleHandler-";

    PostGeneralFormDTO postGeneralForm(PostGeneralFormCommand cmd);

    GeneralFormDTO getTemplateBySourceId(GetTemplateBySourceIdCommand cmd);
}
