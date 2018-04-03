package com.everhomes.rest.flow;

public enum FlowScriptType {
	PROTOTYPE("prototype"), BEAN_ID("bean_id");
	
	private String code;
    private FlowScriptType(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static FlowScriptType fromCode(String code) {
    	if(code == null) {
    		return null;
    	}
    	
    	for(FlowScriptType t : FlowScriptType.values()) {
    		if(code.equalsIgnoreCase(t.getCode())) {
    			return t;
    		}
    	}

        return null;
    }
}
