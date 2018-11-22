package com.everhomes.module.security;

import com.everhomes.acl.WebMenu;
import com.everhomes.acl.WebMenuPrivilegeProvider;
import com.everhomes.bigcollection.Accessor;
import com.everhomes.bigcollection.BigCollectionProvider;
import com.everhomes.configuration.ConfigConstants;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.rest.acl.WebMenuType;
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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 *  注：该密码模块主要作用类似客户端的屏保功能，由前端保存登陆状态，后台不保存
 */
@Component
public class ServiceModuleSecurityServiceImpl implements ServiceModuleSecurityService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceModuleSecurityServiceImpl.class);
    @Autowired
    private ServiceModuleSecurityProvider serviceModuleSecurityProvider;
    @Autowired
    private UserProvider userProvider;
    @Autowired
    private ConfigurationProvider configurationProvider;
    @Autowired
    protected BigCollectionProvider bigCollectionProvider;
    @Autowired
    private WebMenuPrivilegeProvider webMenuProvider;

    private ValueOperations<String, String> getValueOperations(String key) {
        final StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        Accessor acc = bigCollectionProvider.getMapAccessor(key, "");
        RedisTemplate redisTemplate = acc.getTemplate(stringRedisSerializer);
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();

        return valueOperations;
    }

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
        // 屏保功能由客户端保存登陆状态，后台暂不保存
        // refreshModuleSecurityVerifyInfo(cmd.getOwnerId(), cmd.getOwnerType(), cmd.getModuleId(), request, response);
    }

    @Override
    public void refreshModuleSecurityVerifyInfo(Long ownerId, String ownerType, Long moduleId, HttpServletRequest request, HttpServletResponse response) {
        try {
            Cookie cookie = findCookieInRequest(SERVICE_MODULE_SECURITY_SESSION_ID, request);
            String smsSessionId = null;
            if (cookie == null) {
                smsSessionId = generateSessionId();
                setCookieInResponse(SERVICE_MODULE_SECURITY_SESSION_ID, smsSessionId, request, response);
            } else {
                smsSessionId = cookie.getValue();
            }

            int expiry = configurationProvider.getIntValue(ConfigConstants.SERVICE_MODULE_SECURITY_TIME_OUT + "-" + moduleId, 5);
            String key = String.format("ServiceModule-%s-%d-%s", smsSessionId, UserContext.currentUserId(), moduleId);
            ValueOperations<String, String> valueOperations = getValueOperations(key);
            valueOperations.set(key, String.format("%s-%s", ownerId, ownerType), expiry, TimeUnit.MINUTES);
        } catch (Exception e) {
            LOGGER.error("buildModuleSecurityCookieName error", e);
        }
    }

    @Override
    public boolean isModuleSecurityVerified(Long ownerId, String ownerType, Long moduleId, HttpServletRequest request) {
        Cookie cookie = findCookieInRequest(SERVICE_MODULE_SECURITY_SESSION_ID, request);
        if (cookie == null) {
            return false;
        }
        String key = String.format("ServiceModule-%s-%d-%s", cookie.getValue(), UserContext.currentUserId(), moduleId);
        ValueOperations<String, String> valueOperations = getValueOperations(key);
        return String.format("%s-%s", ownerId, ownerType).equals(valueOperations.get(key));
    }

    @Override
    public void setReturnUrlCookie(Long moduleId, HttpServletRequest request, HttpServletResponse response) {
        List<WebMenu> webMenus = webMenuProvider.listMenuByModuleIdAndType(moduleId, WebMenuType.PARK.getCode());
        if (webMenus != null && webMenus.size() > 0) {
            setCookieInResponse(SERVICE_MODULE_RETURN_URL, webMenus.get(0).getDataType(), request, response);
        }
    }

    private static void setCookieInResponse(String name, String value, HttpServletRequest request, HttpServletResponse response) {
        Cookie cookie = findCookieInRequest(name, request);
        if (cookie == null) {
            cookie = new Cookie(name, value);
        } else {
            cookie.setValue(value);
        }
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    private static Cookie findCookieInRequest(String name, HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    return cookie;
                }
            }
        }
        return null;
    }

    private String generateSessionId() {
        return UUID.randomUUID().toString().replace("-", "");
    }

}
