package com.everhomes.authorization;

import com.everhomes.rest.general_approval.PostGeneralFormValCommand;
import com.everhomes.rest.general_approval.PostGeneralFormDTO;

/**
 * 
 */
public interface AuthorizationModuleHandler {

    String GENERAL_FORM_MODULE_HANDLER_PREFIX = "AuthorizationModuleHandler-";
    
    String PERSONAL_AUTHORIZATION = "1";
    String ORGANIZATION_AUTHORIZATION = "2";
    
    PostGeneralFormDTO personalAuthorization(PostGeneralFormValCommand cmd);
    
    PostGeneralFormDTO organiztionAuthorization(PostGeneralFormValCommand cmd);
  
}
