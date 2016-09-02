// @formatter:off
package com.everhomes.rest.pmtask;

/**
 * <ul>
 * <li>USER("user"): 用户</li>
 * </ul>
 */
public enum PmTaskTargetType {
    USER("user");
    
    private String code;
    private PmTaskTargetType(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static PmTaskTargetType fromCode(String code) {
        if(code != null) {
            PmTaskTargetType[] values = PmTaskTargetType.values();
            for(PmTaskTargetType value : values) {
                if(code.equals(value.code)) {
                    return value;
                }
            }
        }
        
        return null;
    }
}
