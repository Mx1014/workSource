package com.everhomes.rest.flow;

public enum FlowOwnerType {
	ENTERPRISE("enterprise"), USER("user"), GROUP("group"), PM("pm"), DEPARTMENT("department"), PARKING("parking");
	
	private String code;
    private FlowOwnerType(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static FlowOwnerType fromCode(String code) {
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
