package com.everhomes.rest.organization;

/**
 * <ul>机构任务被指派的目标类型
 * <li>USER: 用户</li>
 * </ul>
 */
public enum OrganizationTaskTargetType {
    USER("USER");
    
    private String code;
    private OrganizationTaskTargetType(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static OrganizationTaskTargetType fromCode(String code) {
        OrganizationTaskTargetType[] values = OrganizationTaskTargetType.values();
        for(OrganizationTaskTargetType value : values) {
            if(value.code.equals(code)) {
                return value;
            }
        }
        
        return null;
    }
}