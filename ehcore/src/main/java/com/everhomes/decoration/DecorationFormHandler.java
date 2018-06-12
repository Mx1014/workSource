package com.everhomes.decoration;

import com.everhomes.general_form.GeneralFormModuleHandler;
import com.everhomes.rest.decoration.PostApprovalFormCommand;
import com.everhomes.rest.general_approval.GeneralFormDTO;
import com.everhomes.rest.general_approval.GetTemplateBySourceIdCommand;
import com.everhomes.rest.general_approval.PostGeneralFormDTO;
import com.everhomes.rest.general_approval.PostGeneralFormValCommand;
import com.everhomes.util.ConvertHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component(GeneralFormModuleHandler.GENERAL_FORM_MODULE_HANDLER_PREFIX + "EhDecoration")
public class DecorationFormHandler implements GeneralFormModuleHandler {
    @Autowired
    private DecorationProvider decorationProvider;
    @Autowired
    private DecorationService decorationService;

    @Override
    public PostGeneralFormDTO postGeneralFormVal(PostGeneralFormValCommand cmd) {

        PostApprovalFormCommand cmd2 = new PostApprovalFormCommand();
        cmd2.setRequestId(cmd.getSourceId());
        cmd2.setValues(cmd.getValues());
        cmd2.setApprovalId(cmd.getOwnerId());
        decorationService.postApprovalForm(cmd2);
        PostGeneralFormDTO dto = ConvertHelper.convert(cmd,PostGeneralFormDTO.class);
        return dto;
    }

    @Override
    public GeneralFormDTO getTemplateBySourceId(GetTemplateBySourceIdCommand cmd) {
        return null;
    }

    @Override
    public PostGeneralFormDTO updateGeneralFormVal(PostGeneralFormValCommand cmd) {
        return null;
    }
}
