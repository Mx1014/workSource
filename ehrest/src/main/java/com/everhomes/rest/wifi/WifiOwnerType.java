// @formatter:off
package com.everhomes.rest.wifi;

/**
 * <p>归属类型</p>
 * <ul>
 * <li>COMMUNITY("community"): 小区</li>
 * </ul>
 */
public enum WifiOwnerType {
    COMMUNITY("community"),ORGAMIZATION("EhOrganizations");
    
    private String code;
    private WifiOwnerType(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static WifiOwnerType fromCode(String code) {
        if(code != null) {
            WifiOwnerType[] values = WifiOwnerType.values();
            for(WifiOwnerType value : values) {
                if(code.equals(value.code)) {
                    return value;
                }
            }
        }
        
        return null;
    }
}
