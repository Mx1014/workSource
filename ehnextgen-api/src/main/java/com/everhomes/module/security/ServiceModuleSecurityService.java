package com.everhomes.module.security;

import com.everhomes.rest.module.security.GetServiceModuleSecurityStatusCommand;
import com.everhomes.rest.module.security.GetServiceModuleSecurityStatusResponse;
import com.everhomes.rest.module.security.ServiceModuleSecurityResetCommand;
import com.everhomes.rest.module.security.ServiceModuleSecuritySettingCommand;
import com.everhomes.rest.module.security.ServiceModuleSecurityVerifyCommand;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface ServiceModuleSecurityService {

    GetServiceModuleSecurityStatusResponse getServiceModuleSecurityStatus(GetServiceModuleSecurityStatusCommand cmd, HttpServletRequest request);

    void serviceModuleSecuritySetting(ServiceModuleSecuritySettingCommand cmd);

    void serviceModuleSecurityReset(ServiceModuleSecurityResetCommand cmd);

    void serviceModuleSecurityVerify(ServiceModuleSecurityVerifyCommand cmd, HttpServletRequest request, HttpServletResponse response);

}
