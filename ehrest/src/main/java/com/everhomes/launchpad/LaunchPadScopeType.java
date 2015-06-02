// @formatter:off
package com.everhomes.launchpad;


/**
 * <ul>参数类型
 * <li>COUNTRY: 全国</li>
 * <li>CITY: 城市</li>
 * <li>COMMUNITY: 小区</li>
 * <li>USERDEFINED: 用户自定义</li>
 * </ul>
 */
public enum LaunchPadScopeType {
    COUNTRY("Country"),CITY("City"), COMMUNITY("Community"), USERDEFINED("UserDefined");
    
 private String code;
    
    private LaunchPadScopeType(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static LaunchPadScopeType fromCode(String code) {
        if(code == null)
            return null;

        if(code.equalsIgnoreCase(COUNTRY.getCode()))
            return COUNTRY;
        else if(code.equalsIgnoreCase(CITY.getCode()))
            return CITY;
        else if(code.equalsIgnoreCase(COMMUNITY.getCode()))
            return COMMUNITY;
        else if(code.equalsIgnoreCase(USERDEFINED.getCode()))
            return USERDEFINED;
        
        return null;
    }
}
