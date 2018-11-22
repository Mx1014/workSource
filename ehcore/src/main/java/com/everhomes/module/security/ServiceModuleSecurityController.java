package com.everhomes.module.security;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.module.security.GetServiceModuleSecurityStatusCommand;
import com.everhomes.rest.module.security.GetServiceModuleSecurityStatusResponse;
import com.everhomes.rest.module.security.ServiceModuleSecurityResetCommand;
import com.everhomes.rest.module.security.ServiceModuleSecuritySettingCommand;
import com.everhomes.rest.module.security.ServiceModuleSecurityVerifyCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestDoc(value = "module-security controller", site = "core")
@RestController
@RequestMapping("/module/security")
public class ServiceModuleSecurityController extends ControllerBase {
    @Autowired
    private ServiceModuleSecurityService securityService;

    /**
     * <b>URL: /module/security/getStatus</b>
     * <p>获取模块安全设置状态</p>
     */
    @RequestMapping("getStatus")
    @RestReturn(value = GetServiceModuleSecurityStatusResponse.class)
    public RestResponse getServiceModuleSecurityStatus(GetServiceModuleSecurityStatusCommand cmd, HttpServletRequest request) {
        RestResponse response = new RestResponse(securityService.getServiceModuleSecurityStatus(cmd, request));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /module/security/password/setting</b>
     * <p>模块安全密码设置</p>
     */
    @RequestMapping("password/setting")
    @RestReturn(value = String.class)
    public RestResponse serviceModuleSecuritySetting(ServiceModuleSecuritySettingCommand cmd) {
        securityService.serviceModuleSecuritySetting(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /module/security/password/reset</b>
     * <p>模块安全密码重置</p>
     */
    @RequestMapping("password/reset")
    @RestReturn(value = String.class)
    public RestResponse serviceModuleSecurityReset(ServiceModuleSecurityResetCommand cmd) {
        securityService.serviceModuleSecurityReset(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /module/security/password/verify</b>
     * <p>模块安全密码登陆验证</p>
     */
    @RequestMapping("password/verify")
    @RestReturn(value = String.class)
    public RestResponse serviceModuleSecurityVerify(ServiceModuleSecurityVerifyCommand cmd, HttpServletRequest request, HttpServletResponse httpServletResponse) {
        securityService.serviceModuleSecurityVerify(cmd, request, httpServletResponse);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
}
