// @formatter:off
package com.everhomes.organization.pm;

import com.everhomes.user.User;

public enum PmTargetType {
    USER(User.class.getName());
    
    private String code;
    private PmTargetType(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static PmTargetType fromCode(String code) {
    	if(code == null)
            return null;
        
        if(code.equalsIgnoreCase(USER.getCode())) {
        	return USER;
        }
        
        return null;
    }
}
