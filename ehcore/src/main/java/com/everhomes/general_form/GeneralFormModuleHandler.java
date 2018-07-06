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

    default GeneralFormReminderDTO getGeneralFormReminder(GeneralFormReminderCommand cmd){
        return new GeneralFormReminderDTO(TrueOrFalseFlag.FALSE.getCode());
    }

    /**
     * 只保存表单数据但不提交工作流
     * @param cmd
     */
    default PostGeneralFormDTO saveGeneralFormVal(PostGeneralFormValCommand cmd){
        return new PostGeneralFormDTO();
    }
}
