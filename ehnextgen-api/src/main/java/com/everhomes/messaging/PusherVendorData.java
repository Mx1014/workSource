package com.everhomes.messaging;

import com.everhomes.util.StringHelper;

public class PusherVendorData {
    private String appId;
    private String appSecret;
    private String appPkgName;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getAppPkgName() {
        return appPkgName;
    }

    public void setAppPkgName(String appPkgName) {
        this.appPkgName = appPkgName;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
