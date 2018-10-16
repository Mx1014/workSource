package com.everhomes.rest.app;

/**
 * <ul>
 *     <li>realAppKey: 兼容原来的客户端，保留</li>
 * </ul>
 */
public class GetAppCommand {

    private String realAppKey;

    public String getRealAppKey() {
        return realAppKey;
    }

    public void setRealAppKey(String realAppKey) {
        this.realAppKey = realAppKey;
    }
}