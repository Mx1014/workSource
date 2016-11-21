package com.everhomes.rest.flow;

public enum FlowUserBelongType {
	FLOW_SUPERVISER("flow_superviser"), FLOW_NODE_PROCESSOR("flow_node_processor"), FLOW_NODE_APPLIER("flow_node_applier");
	
	private String code;
    private FlowUserBelongType(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static FlowUserBelongType fromCode(String code) {
    	if(code == null) {
    		return null;
    	}
    	
    	for(FlowUserBelongType t : FlowUserBelongType.values()) {
    		if(code.equalsIgnoreCase(t.getCode())) {
    			return t;
    		}
    	}

        return null;
    }
}
