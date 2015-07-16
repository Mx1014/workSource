package com.everhomes.organization;

/**
 * <ul>承载机构任务的实体类型
 * <li>TOPIC: 帖子</li>
 * </ul>
 */
public enum OrganizationTaskEnityType {
	TOPIC("TOPIC");
    
    private String code;
    private OrganizationTaskEnityType(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static OrganizationTaskEnityType fromCode(String code) {
        OrganizationTaskEnityType[] values = OrganizationTaskEnityType.values();
        for(OrganizationTaskEnityType value : values) {
            if(value.code.equals(code)) {
                return value;
            }
        }
        
        return null;
    }
}