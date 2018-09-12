package com.everhomes.core.sdk;

import com.everhomes.util.StringHelper;

public class CoreSdkSettings {

    private final static CoreSdkSettings setting = new CoreSdkSettings();

    private String appKey;
    private String secretKey;
    private String connectUrl;
    private String systemTag;

    public static CoreSdkSettings getInstance() {
        return setting;
    }

    public static void init(String appKey, String secretKey, String connectUrl, String systemTag) {
        setting.appKey = appKey;
        setting.secretKey = secretKey;
        setting.connectUrl = connectUrl;
        setting.systemTag = systemTag;
    }

    public String getAppKey() {
        return appKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public String getConnectUrl() {
        return connectUrl;
    }

    public String getSystemTag() {
        return systemTag;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
