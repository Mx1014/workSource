package com.everhomes.rest.organization;

/**
 * <ul>机构任务创建者实体类型
 * <li>USER: 普通用户创建任务</li>
 * <li>ORGANIZATION: 机构成员创建任务</li>
 * </ul>
 */
public enum OrganizationTaskCreatorEnityType {
	USER("USER"), ORGANIZATION("ORGANIZATION");
    
    private String code;
    private OrganizationTaskCreatorEnityType(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static OrganizationTaskCreatorEnityType fromCode(String code) {
        OrganizationTaskCreatorEnityType[] values = OrganizationTaskCreatorEnityType.values();
        for(OrganizationTaskCreatorEnityType value : values) {
            if(value.code.equals(code)) {
                return value;
            }
        }
        
        return null;
    }
}