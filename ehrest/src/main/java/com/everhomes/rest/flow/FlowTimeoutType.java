package com.everhomes.rest.flow;

public enum FlowTimeoutType {
	STEP_TIMEOUT("step_timeout"), REMIND_TIMEOUT("remind_timeout");
	
	private String code;
    private FlowTimeoutType(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static FlowTimeoutType fromCode(String code) {
    	if(code == null) {
    		return null;
    	}
    	
    	for(FlowTimeoutType t : FlowTimeoutType.values()) {
    		if(code.equalsIgnoreCase(t.getCode())) {
    			return t;
    		}
    	}

        return null;
    }
}
