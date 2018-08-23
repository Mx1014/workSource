package com.everhomes.rest.app;

/**
 * <ul>
 *     <li>thirdPartyAppKey: 新的客户端传的第三方 appKey</li>
 *     <li>appKey: 兼容原来的客户端，保留</li>
 * </ul>
 */
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