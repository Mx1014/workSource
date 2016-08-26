package com.everhomes.user;

import com.everhomes.rest.user.LoginToken;
import com.everhomes.util.StringHelper;

public class LogonRef {
    private UserLogin foundLogin;
    private Integer nextLoginId;
    private Integer namespaceId;
    private Long impId;
    private String oldDeviceId;
    private LoginToken oldLoginToken;
    
    public UserLogin getFoundLogin() {
        return foundLogin;
    }
    public void setFoundLogin(UserLogin foundLogin) {
        this.foundLogin = foundLogin;
    }
    public Integer getNextLoginId() {
        return nextLoginId;
    }
    public void setNextLoginId(Integer nextLoginId) {
        this.nextLoginId = nextLoginId;
    }
    public Integer getNamespaceId() {
        return namespaceId;
    }
    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }
    public String getOldDeviceId() {
        return oldDeviceId;
    }
    public void setOldDeviceId(String oldDeviceId) {
        this.oldDeviceId = oldDeviceId;
    }

    public LoginToken getOldLoginToken() {
        return oldLoginToken;
    }
    public void setOldLoginToken(LoginToken oldLoginToken) {
        this.oldLoginToken = oldLoginToken;
    }

    public Long getImpId() {
        return impId;
    }
    public void setImpId(Long impId) {
        this.impId = impId;
    }
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
