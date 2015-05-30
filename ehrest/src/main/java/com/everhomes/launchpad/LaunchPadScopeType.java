// @formatter:off
package com.everhomes.launchpad;


/**
 * <ul>参数类型
 * <li>FAMILY: 家庭</li>
 * <li>COMMUNITY: 小区</li>
 * </ul>
 */
public enum LaunchPadScopeType {
    COUNTRY("Country"),CITY("City"), COMMUNITY("Community");
    
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
        
        return null;
    }
}
