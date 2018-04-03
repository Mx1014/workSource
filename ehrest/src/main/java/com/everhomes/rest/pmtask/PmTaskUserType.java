// @formatter:off
package com.everhomes.rest.pmtask;

/**
 * <ul>
 * <li>ADMIN("admin"): 管理员</li>
 * <li>USER("user"): 用户</li>
 * </ul>
 */
public enum PmTaskUserType {
    ADMIN("admin"), USER("user");
    
    private String code;
    private PmTaskUserType(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static PmTaskUserType fromCode(String code) {
        if(code != null) {
            PmTaskUserType[] values = PmTaskUserType.values();
            for(PmTaskUserType value : values) {
                if(code.equals(value.code)) {
                    return value;
                }
            }
        }
        
        return null;
    }
}
