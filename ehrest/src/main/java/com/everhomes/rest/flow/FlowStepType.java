package com.everhomes.rest.flow;

public enum FlowStepType {
	START_STEP("start_step"), APPROVE_STEP("approve_step"), REJECT_STEP("reject_step"), TRANSFER_STEP("transfer_step"), COMMENT_STEP("comment_step"), END_STEP("end_step"), NOTIFY_STEP("notiy_step");
	
	private String code;
    private FlowStepType(String code) {
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
