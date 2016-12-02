package com.everhomes.rest.flow;

public enum FlowEntityType {
	FLOW("flow"), FLOW_NODE("flow_node"), FLOW_ACTION("flow_action"), FLOW_BUTTON("flow_button"), FLOW_SELECTION("flow_selection"), FLOW_USER("flow_user");
	
	private String code;
    private FlowEntityType(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static FlowEntityType fromCode(String code) {
    	if(code == null) {
    		return null;
    	}
    	
    	for(FlowEntityType t : FlowEntityType.values()) {
    		if(code.equalsIgnoreCase(t.getCode())) {
    			return t;
    		}
    	}

        return null;
    }
}
