package com.everhomes.rest.flow;

public enum FlowUserType {
	NO_USER("no_user"), APPLIER("applier"), PROCESSOR("processor");
	
	private String code;
    private FlowUserType(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static FlowUserType fromCode(String code) {
    	if(code == null) {
    		return null;
    	}
    	
    	for(FlowUserType t : FlowUserType.values()) {
    		if(code.equalsIgnoreCase(t.getCode())) {
    			return t;
    		}
    	}

        return null;
    }
}
