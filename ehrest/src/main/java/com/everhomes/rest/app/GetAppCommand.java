package com.everhomes.rest.app;

/**
 * <ul>
 *     <li>appKey: 兼容原来的客户端，保留</li>
 * </ul>
 */
public class GetAppCommand {

    private String appKey;

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }
}