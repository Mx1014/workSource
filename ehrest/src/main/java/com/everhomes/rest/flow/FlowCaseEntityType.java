package com.everhomes.rest.flow;

public enum FlowCaseEntityType {
	LIST("list"), MULTI_LINE("multi_line"), TEXT("text"), IMAGE("image");
	
	private String code;
    private FlowCaseEntityType(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static FlowCaseEntityType fromCode(String code) {
    	if(code == null) {
    		return null;
    	}
    	
    	for(FlowCaseEntityType t : FlowCaseEntityType.values()) {
    		if(code.equalsIgnoreCase(t.getCode())) {
    			return t;
    		}
    	}

        return null;
    }
}
