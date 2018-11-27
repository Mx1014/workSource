package com.everhomes.module.security;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.rest.module.security.GetServiceModuleSecurityStatusCommand;
import com.everhomes.rest.module.security.GetServiceModuleSecurityStatusResponse;
import com.everhomes.rest.module.security.ServiceModuleSecurityResetCommand;
import com.everhomes.rest.module.security.ServiceModuleSecuritySettingCommand;
import com.everhomes.rest.module.security.ServiceModuleSecurityStatus;
import com.everhomes.rest.module.security.ServiceModuleSecurityVerifyCommand;
import com.everhomes.user.EncryptionUtils;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserIdentifier;
import com.everhomes.user.UserProvider;
import com.everhomes.util.DateHelper;
import com.everhomes.util.RuntimeErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 注：该密码模块主要作用类似客户端的屏保功能，登陆状态和登陆超时状态都有前端处理控制，后台只提供密码验证功能，不保存登陆状态
 */
@Component
public class ServiceModuleSecurityServiceImpl implements ServiceModuleSecurityService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceModuleSecurityServiceImpl.class);
    @Autowired
    private ServiceModuleSecurityProvider serviceModuleSecurityProvider;
    @Autowired
    private UserProvider userProvider;


    @Override
    public GetServiceModuleSecurityStatusResponse getServiceModuleSecurityStatus(GetServiceModuleSecurityStatusCommand cmd, HttpServletRequest request) {
        if (cmd.getOwnerId() == null || cmd.getOwnerType() == null || cmd.getModuleId() == null) {
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "params invalid");
        }
        ServiceModuleSecurity existSecurity = serviceModuleSecurityProvider.findServiceModuleSecurity(UserContext.getCurrentNamespaceId(), cmd.getModuleId(), cmd.getOwnerId(), cmd.getOwnerType(), UserContext.currentUserId());
        if (existSecurity == null) {
            return new GetServiceModuleSecurityStatusResponse(ServiceModuleSecurityStatus.NO_SECURITY.getCode());
        }

        return new GetServiceModuleSecurityStatusResponse(ServiceModuleSecurityStatus.SECURITY_SETTED.getCode());
    }

    @Override
    public void serviceModuleSecuritySetting(ServiceModuleSecuritySettingCommand cmd) {
        if (cmd.getOwnerId() == null || cmd.getOwnerType() == null || cmd.getModuleId() == null || !StringUtils.hasText(cmd.getPassword())) {
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "params invalid");
        }
        ServiceModuleSecurity existSecurity = serviceModuleSecurityProvider.findServiceModuleSecurity(UserContext.getCurrentNamespaceId(), cmd.getModuleId(), cmd.getOwnerId(), cmd.getOwnerType(), UserContext.currentUserId());
        if (existSecurity != null) {
            throw RuntimeErrorException.errorWith(ServiceModuleSecurityErrorCodes.SCOPE, ServiceModuleSecurityErrorCodes.SECURITY_PASSWORD_EXIST_ERROR, "security password exist");
        }
        ServiceModuleSecurity security = new ServiceModuleSecurity();
        security.setNamespaceId(UserContext.getCurrentNamespaceId());
        security.setModuleId(cmd.getModuleId());
        security.setOwnerId(cmd.getOwnerId());
        security.setOwnerType(cmd.getOwnerType());
        security.setUserId(UserContext.currentUserId());
        String salt = EncryptionUtils.createRandomSalt();
        security.setSalt(salt);
        security.setPasswordHash(EncryptionUtils.hashPassword(String.format("%s%s", cmd.getPassword(), salt)));

        serviceModuleSecurityProvider.createServiceModuleSecurity(security);
    }

    @Override
    public void serviceModuleSecurityReset(ServiceModuleSecurityResetCommand cmd) {
        if (cmd.getOwnerId() == null || cmd.getOwnerType() == null || cmd.getModuleId() == null || !StringUtils.hasText(cmd.getNewPassword()) || !StringUtils.hasText(cmd.getVerifyCode())) {
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "params invalid");
        }
        ServiceModuleSecurity existSecurity = serviceModuleSecurityProvider.findServiceModuleSecurity(UserContext.getCurrentNamespaceId(), cmd.getModuleId(), cmd.getOwnerId(), cmd.getOwnerType(), UserContext.currentUserId());
        if (existSecurity == null) {
            throw RuntimeErrorException.errorWith(ServiceModuleSecurityErrorCodes.SCOPE, ServiceModuleSecurityErrorCodes.SECURITY_PASSWORD_NO_EXIST_ERROR, "security password no exist");
        }

        UserIdentifier identifier = userProvider.findUserIdentifiersOfUser(UserContext.currentUserId(), UserContext.getCurrentNamespaceId());
        if (identifier == null || !cmd.getVerifyCode().equals(identifier.getVerificationCode())) {
            LOGGER.error("invalid operation,can not find verify information");
            throw RuntimeErrorException.errorWith(ServiceModuleSecurityErrorCodes.SCOPE, ServiceModuleSecurityErrorCodes.ERROR_INVALID_VERIFICATION_CODE, "invalid params");
        }
        // check the expire time
        if (identifier.getNotifyTime() == null || DateHelper.currentGMTTime().getTime() - identifier.getNotifyTime().getTime() > 10 * 60000) {
            LOGGER.error("the verifycode is invalid with timeout");
            throw RuntimeErrorException.errorWith(ServiceModuleSecurityErrorCodes.SCOPE, ServiceModuleSecurityErrorCodes.VERIFY_CODE_TIME_OUT, "verify code time out");
        }

        existSecurity.setPasswordHash(EncryptionUtils.hashPassword(String.format("%s%s", cmd.getNewPassword(), existSecurity.getSalt())));

        serviceModuleSecurityProvider.updateServiceModuleSecurity(existSecurity);
    }

    @Override
    public void serviceModuleSecurityVerify(ServiceModuleSecurityVerifyCommand cmd, HttpServletRequest request, HttpServletResponse response) {
        if (cmd.getOwnerId() == null || cmd.getOwnerType() == null || cmd.getModuleId() == null || !StringUtils.hasText(cmd.getPassword())) {
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "params invalid");
        }
        ServiceModuleSecurity existSecurity = serviceModuleSecurityProvider.findServiceModuleSecurity(UserContext.getCurrentNamespaceId(), cmd.getModuleId(), cmd.getOwnerId(), cmd.getOwnerType(), UserContext.currentUserId());
        if (existSecurity == null) {
            throw RuntimeErrorException.errorWith(ServiceModuleSecurityErrorCodes.SCOPE, ServiceModuleSecurityErrorCodes.SECURITY_PASSWORD_NO_EXIST_ERROR, "security password no exist");
        }
        if (!EncryptionUtils.validateHashPassword(cmd.getPassword(), existSecurity.getSalt(), existSecurity.getPasswordHash())) {
            throw RuntimeErrorException.errorWith(ServiceModuleSecurityErrorCodes.SCOPE, ServiceModuleSecurityErrorCodes.SECURITY_PASSWORD_VERIFY_ERROR, "security password verify error");
        }
    }

}
