package com.everhomes.rest.organization;

/**
 * <ul>承载机构任务的实体类型
 * <li>TOPIC: 帖子</li>
 * </ul>
 */
public enum OrganizationTaskApplyEnityType {
	TOPIC("TOPIC");
    
    private String code;
    private OrganizationTaskApplyEnityType(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static OrganizationTaskApplyEnityType fromCode(String code) {
        OrganizationTaskApplyEnityType[] values = OrganizationTaskApplyEnityType.values();
        for(OrganizationTaskApplyEnityType value : values) {
            if(value.code.equals(code)) {
                return value;
            }
        }
        
        return null;
    }
}