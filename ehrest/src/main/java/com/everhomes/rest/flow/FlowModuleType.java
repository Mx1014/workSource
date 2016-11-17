package com.everhomes.rest.flow;

public enum FlowModuleType {

	NO_MODULE("any-module");
	
	private String code;
    private FlowModuleType(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static FlowModuleType fromCode(String code) {
    	if(code == null) {
    		return null;
    	}
        
        if(code.equalsIgnoreCase(NO_MODULE.getCode())) {
        	return NO_MODULE;
        }

        return null;
    }
}
