// @formatter:off
package com.everhomes.rest.pmtask;

/**
 * <p>归属类型</p>
 * <ul>
 * <li>COMMUNITY("community"): 小区</li>
 * </ul>
 */
public enum PmTaskOwnerType {
    COMMUNITY("community");
    
    private String code;
    private PmTaskOwnerType(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static PmTaskOwnerType fromCode(String code) {
        if(code != null) {
            PmTaskOwnerType[] values = PmTaskOwnerType.values();
            for(PmTaskOwnerType value : values) {
                if(code.equals(value.code)) {
                    return value;
                }
            }
        }
        
        return null;
    }
}
