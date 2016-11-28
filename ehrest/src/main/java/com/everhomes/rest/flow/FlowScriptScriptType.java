package com.everhomes.rest.flow;

public enum FlowScriptScriptType {
	PROTOTYPE("prototype"), BEAN_ID("bean_id");
	
	private String code;
    private FlowScriptScriptType(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static FlowScriptScriptType fromCode(String code) {
    	if(code == null) {
    		return null;
    	}
    	
    	for(FlowScriptScriptType t : FlowScriptScriptType.values()) {
    		if(code.equalsIgnoreCase(t.getCode())) {
    			return t;
    		}
    	}

        return null;
    }
}
