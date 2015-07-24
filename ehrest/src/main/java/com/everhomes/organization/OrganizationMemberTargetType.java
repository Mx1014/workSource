// @formatter:off
package com.everhomes.organization;

/**
 * <ul>
 * <li>USER: 已注册成员</li>
 * <li>UNTRACK: 未注册成员</li>
 * </ul>
 */
public enum OrganizationMemberTargetType {
    USER("user"), UNTRACK("untrack");
    
    private String code;
    private OrganizationMemberTargetType(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static OrganizationMemberTargetType fromCode(String code) {
    	if(code == null) {
    		return null;
    	}
        
        if(code.equalsIgnoreCase(USER.getCode())) {
        	return USER;
        }

        if(code.equalsIgnoreCase(UNTRACK.getCode())) {
        	return UNTRACK;
        }

        
        return null;
    }
}
