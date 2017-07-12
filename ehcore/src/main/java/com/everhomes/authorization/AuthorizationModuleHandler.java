package com.everhomes.authorization;

import com.everhomes.rest.general_approval.PostGeneralFormCommand;
import com.everhomes.rest.general_approval.PostGeneralFormDTO;

/**
 * 
 */
public interface AuthorizationModuleHandler {

    String GENERAL_FORM_MODULE_HANDLER_PREFIX = "AuthorizationModuleHandler-";
    
    PostGeneralFormDTO personalAuthorization(PostGeneralFormCommand cmd);
    
    PostGeneralFormDTO organiztionAuthorization(PostGeneralFormCommand cmd);

}
