package com.everhomes.user;

import javax.validation.constraints.NotNull;

public class LogonCommand {
    @NotNull
    private String userIdentifier;
    
    private String password;
    private String deviceIdentifier;
    
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
}
