package com.everhomes.rest.flow;

public enum FlowLogType {
	BUTTON_FIRED("button_fired"), NODE_ENTER("node enter"), NODE_TRACKER("node_tracker"), FLOW_SUPERVISOR("flow_supervisor");
	
	private String code;
    private FlowLogType(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static FlowLogType fromCode(String code) {
    	if(code == null) {
    		return null;
    	}
    	
    	for(FlowLogType t : FlowLogType.values()) {
    		if(code.equalsIgnoreCase(t.getCode())) {
    			return t;
    		}
    	}

        return null;
    }
}
