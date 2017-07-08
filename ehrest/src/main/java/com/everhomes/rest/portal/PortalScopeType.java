// @formatter:off
package com.everhomes.rest.portal;

/**
 * <ul>范围
 * <li>PM("pm")：无</li>
 * <li>ORGANIZATION("organization"): 普通机构</li>
 * <li>RESIDENTIAL("residential")：小区</li>
 * <li>COMMERCIAL("commercial")：园区</li>
 * </ul>
 */
public enum PortalScopeType {

    PM("pm"),
    ORGANIZATION("organization"),
    RESIDENTIAL("residential"),
    COMMERCIAL("commercial");

    private String code;

    private PortalScopeType(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static PortalScopeType fromCode(String code) {
        if(null != code){
            for (PortalScopeType value: PortalScopeType.values()) {
                if(value.code.equals(code)){
                    return value;
                }
            }
        }
        return null;
    }
}
