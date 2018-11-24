package com.everhomes.module.security;

import com.everhomes.rest.module.security.GetServiceModuleSecurityStatusCommand;
import com.everhomes.rest.module.security.GetServiceModuleSecurityStatusResponse;
import com.everhomes.rest.module.security.ServiceModuleSecurityResetCommand;
import com.everhomes.rest.module.security.ServiceModuleSecuritySettingCommand;
import com.everhomes.rest.module.security.ServiceModuleSecurityVerifyCommand;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface ServiceModuleSecurityService {
    String SERVICE_MODULE_SECURITY_SESSION_ID = "SERVICE_MODULE_SESSION_ID";
    String SERVICE_MODULE_RETURN_URL = "SERVICE_MODULE_DATA_TYPE";

    GetServiceModuleSecurityStatusResponse getServiceModuleSecurityStatus(GetServiceModuleSecurityStatusCommand cmd, HttpServletRequest request);

    void serviceModuleSecuritySetting(ServiceModuleSecuritySettingCommand cmd);

    void serviceModuleSecurityReset(ServiceModuleSecurityResetCommand cmd);

    void serviceModuleSecurityVerify(ServiceModuleSecurityVerifyCommand cmd, HttpServletRequest request, HttpServletResponse response);

    void refreshModuleSecurityVerifyInfo(Long ownerId, String ownerType, Long moduleId, HttpServletRequest request, HttpServletResponse response);

    boolean isModuleSecurityVerified(Long ownerId, String ownerType, Long moduleId, HttpServletRequest request);

    void setReturnUrlCookie(Long moduleId, HttpServletRequest request, HttpServletResponse response);

}
