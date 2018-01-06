package com.everhomes.authorization;

import org.springframework.stereotype.Component;

import com.everhomes.general_form.GeneralFormModuleHandler;
import com.everhomes.rest.general_approval.PostGeneralFormValCommand;
import com.everhomes.rest.general_approval.PostGeneralFormDTO;

@Component(GeneralFormModuleHandler.GENERAL_FORM_MODULE_HANDLER_PREFIX +"personal_auth")
public class PersonalAuthorizationFormHandler extends AuthorizationFormHandler {
	@Override
    public PostGeneralFormDTO postGeneralFormVal(PostGeneralFormValCommand cmd) {
        AuthorizationModuleHandler handler = getAuthorizationHandler(cmd.getOwnerType()+cmd.getOwnerId());
        return handler.personalAuthorization(cmd);
    }

    @Override
    public PostGeneralFormDTO updateGeneralFormVal(PostGeneralFormValCommand cmd) {
        return null;
    }
}
