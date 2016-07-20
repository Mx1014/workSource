package com.everhomes.rest.quality;


/**
 * <ul>
 * <li>ENTERPRISE: 公司</li>
 * <li>USER: 用户</li>
 * <li>GROUP: 机构成员</li>
 * </ul>
 */
public enum OwnerType {

	ENTERPRISE("enterprise"), USER("user"), GROUP("group");
	
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

        return null;
    }
}
