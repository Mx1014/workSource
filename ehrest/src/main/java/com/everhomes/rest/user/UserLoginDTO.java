package com.everhomes.rest.user;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>userId: 用户ID</li>
 * <li>loginId: 登录ID</li>
 * <li>deviceIdentifier: 设备ID号</li>
 * <li>namespaceId: 域空间ID</li>
 * <li>status: 状态 UserLoginStatus, 0: 登出， 1： 登入中</li>
 * <li>loginBorderId: 当前 borderId </li>
 * <li>lastAccessTick: 上次访问时间</li>
 * <li>pusherIdentify: 推送标识</li>
 * <li>isOnline: 是否在线 </li>
 * <li>deviceType: Android/iOS/其它</li>
 * </ul>
 * @author janson
 *
 */
public class UserLoginDTO {
    private long userId;
    private int loginId;
    private String deviceIdentifier;
    private int namespaceId;
    
    private Byte status;
    private Integer loginBorderId;
    private int loginInstanceNumber;
    private long lastAccessTick;
    
    private Long portalRole;
    
    private Long partnerId;
    
    private String pusherIdentify;
    
    private Byte isOnline;
    private String deviceType;
    private String borderStatus;
    private String borderSessionId;
    
    private Long lastPush;

    public UserLoginDTO() {
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public int getLoginId() {
        return loginId;
    }

    public void setLoginId(int loginId) {
        this.loginId = loginId;
    }

    public String getDeviceIdentifier() {
        return deviceIdentifier;
    }

    public void setDeviceIdentifier(String deviceIdentifier) {
        this.deviceIdentifier = deviceIdentifier;
    }

    public int getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(int namespaceId) {
        this.namespaceId = namespaceId;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Integer getLoginBorderId() {
        return loginBorderId;
    }

    public void setLoginBorderId(Integer loginBorderId) {
        this.loginBorderId = loginBorderId;
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

    public Long getPortalRole() {
        return portalRole;
    }

    public void setPortalRole(Long portalRole) {
        this.portalRole = portalRole;
    }

    public Long getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(Long partnerId) {
        this.partnerId = partnerId;
    }
    
    public String getPusherIdentify() {
        return pusherIdentify;
    }

    public void setPusherIdentify(String pusherIdentify) {
        this.pusherIdentify = pusherIdentify;
    }

    public Byte getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(Byte isOnline) {
        this.isOnline = isOnline;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    
    public String getBorderStatus() {
        return borderStatus;
    }

    public void setBorderStatus(String borderStatus) {
        this.borderStatus = borderStatus;
    }


    public String getBorderSessionId() {
        return borderSessionId;
    }

    public void setBorderSessionId(String borderSessionId) {
        this.borderSessionId = borderSessionId;
    }

    public Long getLastPush() {
        return lastPush;
    }

    public void setLastPush(Long lastPush) {
        this.lastPush = lastPush;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
