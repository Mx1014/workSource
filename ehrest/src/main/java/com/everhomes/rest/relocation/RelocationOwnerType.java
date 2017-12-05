// @formatter:off
package com.everhomes.rest.relocation;

/**
 * <p>归属类型</p>
 * <ul>
 * <li>COMMUNITY("community"): 小区</li>
 * </ul>
 */
public enum RelocationOwnerType {
    COMMUNITY("community"), ORGANIZATION("organization");

    private String code;
    private RelocationOwnerType(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static RelocationOwnerType fromCode(String code) {
        if(code != null) {
            RelocationOwnerType[] values = RelocationOwnerType.values();
            for(RelocationOwnerType value : values) {
                if(code.equals(value.code)) {
                    return value;
                }
            }
        }
        
        return null;
    }
}
