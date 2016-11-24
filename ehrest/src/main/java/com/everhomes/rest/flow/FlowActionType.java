package com.everhomes.rest.flow;

public enum FlowActionType {
	MESSAGE("message"), SMS("sms"), TICK_MESSAGE("tick_message"), TICK_SMS("tick_sms"), TRACK("track"), ENTER_SCRIPT("enter_script");
	
	private String code;
    private FlowActionType(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static FlowActionType fromCode(String code) {
    	if(code == null) {
    		return null;
    	}
    	
    	for(FlowActionType t : FlowActionType.values()) {
    		if(code.equalsIgnoreCase(t.getCode())) {
    			return t;
    		}
    	}

        return null;
    }
}
