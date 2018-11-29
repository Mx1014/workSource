package com.everhomes.general_approval;

import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.general_form.GeneralFormModuleHandler;
import com.everhomes.general_form.GeneralFormService;
import com.everhomes.rest.flow.FlowModuleType;
import com.everhomes.rest.general_approval.CreateOrUpdateGeneralFormValuesWithFlowCommand;
import com.everhomes.rest.general_approval.GeneralFormDTO;
import com.everhomes.rest.general_approval.GeneralFormReminderCommand;
import com.everhomes.rest.general_approval.GeneralFormReminderDTO;
import com.everhomes.rest.general_approval.GetGeneralFormsAndValuesByFlowNodeCommand;
import com.everhomes.rest.general_approval.GetTemplateBySourceIdCommand;
import com.everhomes.rest.general_approval.ListGeneralFormResponse;
import com.everhomes.rest.general_approval.PostApprovalFormCommand;
import com.everhomes.rest.general_approval.PostGeneralFormDTO;
import com.everhomes.rest.general_approval.PostGeneralFormValCommand;
import com.everhomes.rest.general_approval.*;
import com.everhomes.yellowPage.YellowPageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component(GeneralFormModuleHandler.GENERAL_FORM_MODULE_HANDLER_PREFIX + "GENERAL_APPROVE")
public class GeneralApprovalFormModuleHandler implements GeneralFormModuleHandler {

    @Autowired
    private GeneralApprovalProvider generalApprovalProvider;
    
    @Autowired
    GeneralFormService generalFormService;

    @Override
    public PostGeneralFormDTO postGeneralFormVal(PostGeneralFormValCommand cmd) {
    	
        GeneralApprovalFormHandler handler = getApprovalPostItemHandler(cmd.getSourceId());
        PostApprovalFormCommand command = new PostApprovalFormCommand();
        command.setApprovalId(cmd.getSourceId());
        command.setValues(cmd.getValues());
        command.setOrganizationId(cmd.getCurrentOrganizationId());
        return handler.postApprovalForm(command);
    }

    private GeneralApprovalFormHandler getApprovalPostItemHandler(Long approvalId) {
        GeneralApproval ga = generalApprovalProvider.getGeneralApprovalById(approvalId);
        if (ga != null) {
            GeneralApprovalFormHandler handler = PlatformContext.getComponent(GeneralApprovalFormHandler.GENERAL_APPROVAL_FORM_PREFIX +
                    ga.getModuleType());
            if (handler != null)
                return handler;
        }
        return PlatformContext.getComponent(GeneralApprovalFormHandler.GENERAL_APPROVAL_FORM_PREFIX + FlowModuleType.NO_MODULE.getCode());
    }


    @Override
    public GeneralFormDTO getTemplateBySourceId(GetTemplateBySourceIdCommand cmd) {
    	
        GeneralApprovalFormHandler handler = getApprovalPostItemHandler(cmd.getSourceId());
        return handler.getTemplateBySourceId(cmd);
    }

    @Override
    public PostGeneralFormDTO updateGeneralFormVal(PostGeneralFormValCommand cmd) {
        return null;
    }

    @Override
    public GeneralFormReminderDTO getGeneralFormReminder(GeneralFormReminderCommand cmd){
        GeneralApprovalFormHandler handler = getApprovalPostItemHandler(cmd.getSourceId());
        return handler.getGeneralFormReminder(cmd);
    }

}
