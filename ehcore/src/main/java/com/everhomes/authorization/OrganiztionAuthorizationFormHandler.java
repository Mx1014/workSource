package com.everhomes.authorization;

import org.springframework.stereotype.Component;

import com.everhomes.general_form.GeneralFormModuleHandler;
import com.everhomes.rest.general_approval.PostGeneralFormCommand;
import com.everhomes.rest.general_approval.PostGeneralFormDTO;

@Component(GeneralFormModuleHandler.GENERAL_FORM_MODULE_HANDLER_PREFIX +"zj_organization_auth")
public class OrganiztionAuthorizationFormHandler extends AuthorizationFormHandler {

    @Override
    public PostGeneralFormDTO postGeneralForm(PostGeneralFormCommand cmd) {
        AuthorizationModuleHandler handler = getAuthorizationHandler(cmd.getOwnerType()+cmd.getOwnerId());
        return handler.organiztionAuthorization(cmd);
    }
}
