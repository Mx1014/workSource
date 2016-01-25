// @formatter:off
package com.everhomes.rest.organization;

/**
 * <ul>
 * <li>USER: 已注册成员</li>
 * <li>UNTRACK: 未注册成员</li>
 * <li>CONTACT: 来自通讯录</li>
 * </ul>
 */
public enum OrganizationMemberTargetType {
    USER("USER"), UNTRACK("UNTRACK"), CONTACT("CONTACT");
    
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

        if(code.equalsIgnoreCase(CONTACT.getCode())) {
        	return CONTACT;
        }
        
        return null;
    }
}
