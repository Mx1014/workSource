// @formatter:off
package com.everhomes.rest.pmsy;

/**
 * <p>归属类型</p>
 * <ul>
 * <li>COMMUNITY("community"): 小区</li>
 * </ul>
 */
public enum PmsyOwnerType {
    COMMUNITY("community");
    
    private String code;
    private PmsyOwnerType(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static PmsyOwnerType fromCode(String code) {
        if(code != null) {
            PmsyOwnerType[] values = PmsyOwnerType.values();
            for(PmsyOwnerType value : values) {
                if(code.equals(value.code)) {
                    return value;
                }
            }
        }
        
        return null;
    }
}
