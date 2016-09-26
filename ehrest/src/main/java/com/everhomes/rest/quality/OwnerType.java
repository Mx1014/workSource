package com.everhomes.rest.quality;


/**
 * <ul>
 * <li>ENTERPRISE: 公司</li>
 * <li>USER: 用户</li>
 * <li>GROUP: 机构成员</li>
 * <li>PM: 物业</li>
 * <li>DEPARTMENT: 部门</li>
 * </ul>
 */
public enum OwnerType {

	ENTERPRISE("enterprise"), USER("user"), GROUP("group"), PM("pm"), DEPARTMENT("department");
	
	private String code;
    private OwnerType(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static OwnerType fromCode(String code) {
    	if(code == null) {
    		return null;
    	}
        
        if(code.equalsIgnoreCase(ENTERPRISE.getCode())) {
        	return ENTERPRISE;
        }

        if(code.equalsIgnoreCase(USER.getCode())) {
        	return USER;
        }
        
        if(code.equalsIgnoreCase(GROUP.getCode())) {
        	return GROUP;
        }
        
        if(code.equalsIgnoreCase(PM.getCode())) {
        	return PM;
        }
        
        if(code.equalsIgnoreCase(DEPARTMENT.getCode())) {
        	return DEPARTMENT;
        }

        return null;
    }
}
