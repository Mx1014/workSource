// @formatter:off
package com.everhomes.rest.reserve;

/**
 * <p>归属类型</p>
 * <ul>
 * <li>COMMUNITY("community"): 小区</li>
 * <li>ORGANIZATION("organization"): 公司</li>
 * </ul>
 */
public enum ReserveOwnerType {
    COMMUNITY("community"), ORGANIZATION("organization");

    private String code;
    private ReserveOwnerType(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static ReserveOwnerType fromCode(String code) {
        if(code != null) {
            ReserveOwnerType[] values = ReserveOwnerType.values();
            for(ReserveOwnerType value : values) {
                if(code.equals(value.code)) {
                    return value;
                }
            }
        }
        
        return null;
    }
}
