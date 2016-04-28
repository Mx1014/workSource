// @formatter:off
package com.everhomes.rest.pmsy;

/**
 * <p>归属类型</p>
 * <ul>
 * <li>COMMUNITY("community"): 小区</li>
 * </ul>
 */
public enum PmOwnerType {
    COMMUNITY("community");
    
    private String code;
    private PmOwnerType(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static PmOwnerType fromCode(String code) {
        if(code != null) {
            PmOwnerType[] values = PmOwnerType.values();
            for(PmOwnerType value : values) {
                if(code.equals(value.code)) {
                    return value;
                }
            }
        }
        
        return null;
    }
}
