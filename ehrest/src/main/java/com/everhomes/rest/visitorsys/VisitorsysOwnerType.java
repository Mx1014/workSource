// @formatter:off
package com.everhomes.rest.visitorsys;

/**
 * <p>访客归属类型</p>
 * <ul>
 * <li>COMMUNITY("community"): 小区</li>
 * <li>ENTERPRISE("enterprise"): 公司</li>
 * </ul>
 */
public enum VisitorsysOwnerType {
    COMMUNITY("community"),
    ENTERPRISE("enterprise");

    private String code;
    VisitorsysOwnerType(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static VisitorsysOwnerType fromCode(String code) {
        if(code != null) {
            VisitorsysOwnerType[] values = VisitorsysOwnerType.values();
            for(VisitorsysOwnerType value : values) {
                if(code.equals(value.code)) {
                    return value;
                }
            }
        }
        
        return null;
    }
}
