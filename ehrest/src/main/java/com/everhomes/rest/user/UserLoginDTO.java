package com.everhomes.rest.user;

import com.everhomes.util.StringHelper;

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

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
