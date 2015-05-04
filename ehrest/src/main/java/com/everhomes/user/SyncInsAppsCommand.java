package com.everhomes.user;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

public class SyncInsAppsCommand {
    @NotNull
    private String appName;

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
