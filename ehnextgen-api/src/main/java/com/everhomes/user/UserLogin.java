// @formatter:off
package com.everhomes.user;

import com.everhomes.rest.user.LoginToken;
import com.everhomes.rest.user.UserLoginDTO;
import com.everhomes.rest.user.UserLoginStatus;
import com.everhomes.util.StringHelper;

import java.io.Serializable;
import java.util.Objects;
import java.util.Random;

/**
 * Represents user login session, compared with other data structure like UserIdentifier or User, it is persisted in
 * memory data store(Redis backed big collection), since device disconnects are the normal in mobile network, a memory
 * data store will help reduce the impact of presence information changes.
 * 
 * A user login session is a long-lasting session until explicit logout is issued. Which means, temporary disconnects
 * from client will not be considered as of being logged out, this will ensure that device push notification messages can
 * be correctly issued
 * 
 * @author Kelven Yang
 *
 */
public class UserLogin implements Serializable {
    private static final long serialVersionUID = 7244772864801329872L;

    private long userId;
    private int loginId;
    private String deviceIdentifier;
    private int namespaceId;
    
    private UserLoginStatus status;
    private Integer loginBorderId;
    private int loginInstanceNumber;
    private long lastAccessTick;
    
    private Long portalRole;
    
    //For iOS pusher, added by Janson
    private String pusherIdentify;
    
    private String borderSessionId;
    
    private Long impersonationId;

    private String appVersion;// add by xq.tian 2017/07/14
    
    public UserLogin() {
    }
    
    public UserLogin(int namespaceId, long userId, int loginId, String identifier, String pusherIdentify, String appVersion) {
        this.namespaceId = namespaceId;
        this.userId = userId;
        this.loginId = loginId;
        this.deviceIdentifier = identifier;
        this.appVersion = appVersion;
        loginInstanceNumber = new Random().nextInt();
        lastAccessTick = System.currentTimeMillis();
        status = UserLoginStatus.LOGGED_OFF;
        this.pusherIdentify = pusherIdentify;
    }
    
    public int getNamespaceId() {
        return this.namespaceId;
    }
    
    public void setNamespaceId(int namespaceId) {
        this.namespaceId = namespaceId;
    }
    
    public Integer getLoginBorderId() {
        return this.loginBorderId;
    }
    
    public void setLoginBorderId(Integer loginBorderId) {
        this.loginBorderId = loginBorderId;
    }
    
    public long getUserId() {
        return this.userId;
    }
    
    public void setUserId(long userId) {
        this.userId = userId;
    }
    
    public int getLoginId() {
        return this.loginId;
    }
    
    public void setLoginId(int loginId) {
        this.loginId = loginId;
    }
    
    public String getDeviceIdentifier() {
        return deviceIdentifier;
    }
    
    public void setDeviceIdentifier(String identifier) {
        this.deviceIdentifier = identifier;
    }
    
    public int getLoginInstanceNumber() {
        return loginInstanceNumber;
    }
    
    public void setLoginInstanceNumber(int loginInstanceNumber) {
        this.loginInstanceNumber = loginInstanceNumber;
    }
    
    public long getLastAccessTick() {
        return lastAccessTick;
    }
    
    public void setLastAccessTick(long lastAccessTick) {
        this.lastAccessTick = lastAccessTick;
    }
    
    public UserLoginStatus getStatus() {
        return this.status;
    }
    
    /**
     * 注意这个函数会导致 loginInstanceNumber
     * @param status
     */
    public void setStatus(UserLoginStatus status) {
        this.status = status;
        
        if(status == UserLoginStatus.LOGGED_OFF) {
            this.loginInstanceNumber++;
        }
    }
    
    public Long getPortalRole() {
        return portalRole;
    }

    public void setPortalRole(Long portalRole) {
        this.portalRole = portalRole;
    }

    public LoginToken getLoginToken() {
        LoginToken token = new LoginToken(this.userId, this.loginId, this.loginInstanceNumber, this.impersonationId);
        return token;
    }
    
    public UserLoginDTO toDto() {
        UserLoginDTO dto = new UserLoginDTO();
        dto.setDeviceIdentifier(this.deviceIdentifier);
        dto.setLastAccessTick(this.lastAccessTick);
        dto.setLoginBorderId(this.loginBorderId);
        dto.setLoginId(this.loginId);
        dto.setLoginInstanceNumber(this.loginInstanceNumber);
        dto.setNamespaceId(this.namespaceId);
        dto.setPortalRole(this.portalRole);
        dto.setStatus(this.status != null ? this.status.getCode(): null);
        dto.setUserId(this.userId);
        dto.setPusherIdentify(this.getPusherIdentify());
        dto.setBorderSessionId(this.borderSessionId);
        
        return dto;
    }
    
    public String getPusherIdentify() {
        return pusherIdentify;
    }

    public void setPusherIdentify(String pusherIdentify) {
        this.pusherIdentify = pusherIdentify;
    }

    public String getBorderSessionId() {
        return borderSessionId;
    }

    public void setBorderSessionId(String borderSessionId) {
        this.borderSessionId = borderSessionId;
    }

    public Long getImpersonationId() {
        return impersonationId;
    }

    public void setImpersonationId(Long impersonationId) {
        this.impersonationId = impersonationId;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserLogin userLogin = (UserLogin) o;
        return userId == userLogin.userId &&
                loginId == userLogin.loginId &&
                namespaceId == userLogin.namespaceId &&
                loginInstanceNumber == userLogin.loginInstanceNumber &&
                lastAccessTick == userLogin.lastAccessTick &&
                Objects.equals(deviceIdentifier, userLogin.deviceIdentifier) &&
                status == userLogin.status &&
                Objects.equals(loginBorderId, userLogin.loginBorderId) &&
                Objects.equals(portalRole, userLogin.portalRole) &&
                Objects.equals(pusherIdentify, userLogin.pusherIdentify) &&
                Objects.equals(borderSessionId, userLogin.borderSessionId) &&
                Objects.equals(impersonationId, userLogin.impersonationId) &&
                Objects.equals(appVersion, userLogin.appVersion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, loginId, deviceIdentifier, namespaceId, status, loginBorderId, loginInstanceNumber,
                lastAccessTick, portalRole, pusherIdentify, borderSessionId, impersonationId, appVersion);
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
