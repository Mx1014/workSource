package com.everhomes.rest.flow;

public enum FlowEventType {
	STEP_START("step_start"), STEP_TIMEOUT("step_timeout"), BUTTON_FIRED("button_fired");
	
	private String code;
    private FlowEventType(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static FlowEventType fromCode(String code) {
    	if(code == null) {
    		return null;
    	}
    	
    	for(FlowEventType t : FlowEventType.values()) {
    		if(code.equalsIgnoreCase(t.getCode())) {
    			return t;
    		}
    	}

        return null;
    }
}
