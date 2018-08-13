package com.everhomes.rest.app;

public class TrustedAppCommand {

    private String thirdPartyAppKey;
    private String appKey;

    public String getThirdPartyAppKey() {
        return thirdPartyAppKey;
    }

    public void setThirdPartyAppKey(String thirdPartyAppKey) {
        this.thirdPartyAppKey = thirdPartyAppKey;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }
}