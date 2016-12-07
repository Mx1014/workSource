package com.everhomes.rest.flow;

public enum FlowUserSourceType {
	SOURCE_USER("source_user"), SOURCE_DEPARTMENT("source_department"), SOURCE_POSITION("source_position");
	
	private String code;
    private FlowUserSourceType(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static FlowUserSourceType fromCode(String code) {
    	if(code == null) {
    		return null;
    	}
    	
    	for(FlowUserSourceType t : FlowUserSourceType.values()) {
    		if(code.equalsIgnoreCase(t.getCode())) {
    			return t;
    		}
    	}

        return null;
    }
}
