package com.everhomes.general_form;

import com.everhomes.rest.approval.TrueOrFalseFlag;
import com.everhomes.rest.general_approval.*;

/**
 * <p></p>
 */
public interface GeneralFormModuleHandler {

    String GENERAL_FORM_MODULE_HANDLER_PREFIX = "GeneralFormModuleHandler-";

    PostGeneralFormDTO postGeneralFormVal(PostGeneralFormValCommand cmd);

    GeneralFormDTO getTemplateBySourceId(GetTemplateBySourceIdCommand cmd);

    PostGeneralFormDTO updateGeneralFormVal(PostGeneralFormValCommand cmd);

    default Long saveGeneralFormVal(PostGeneralFormValCommand cmd){
        return null;
    }

    default Long deleteGeneralFormVal(PostGeneralFormValCommand cmd){
        return null;
    }


    default GeneralFormReminderDTO getGeneralFormReminder(GeneralFormReminderCommand cmd){
        return new GeneralFormReminderDTO(TrueOrFalseFlag.FALSE.getCode());
    }
}
