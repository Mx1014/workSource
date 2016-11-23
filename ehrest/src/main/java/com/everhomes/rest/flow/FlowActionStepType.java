package com.everhomes.rest.flow;

public enum FlowActionStepType {
	STEP_NONE("step_none"), STEP_TIMEOUT("step_timeout"), STEP_ENTER("step_enter"), STEP_LEAVE("step_leave");
	
	private String code;
    private FlowActionStepType(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static FlowStepType fromCode(String code) {
    	if(code == null) {
    		return null;
    	}
    	
    	for(FlowStepType t : FlowStepType.values()) {
    		if(code.equalsIgnoreCase(t.getCode())) {
    			return t;
    		}
    	}

        return null;
    }
}
