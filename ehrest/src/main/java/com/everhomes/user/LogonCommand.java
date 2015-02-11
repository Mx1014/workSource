// @formatter:off
package com.everhomes.user;

import javax.validation.constraints.NotNull;

public class LogonCommand {
    @NotNull
    private String userIdentifier;
    
    @NotNull
    private String password;
    
    private String deviceIdentifier;
    
    private Integer namespaceId;
    
    public LogonCommand() {
    }

    public String getUserIdentifier() {
        return userIdentifier;
    }

    public void setUserIdentifier(String userIdentifier) {
        this.userIdentifier = userIdentifier;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDeviceIdentifier() {
        return deviceIdentifier;
    }

    public void setDeviceIdentifier(String deviceIdentifier) {
        this.deviceIdentifier = deviceIdentifier;
    }
    
    public Integer getNamespaceId() {
        return this.namespaceId;
    }
    
    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }
}
