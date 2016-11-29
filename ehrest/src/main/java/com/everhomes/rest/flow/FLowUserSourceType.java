package com.everhomes.rest.flow;

public enum FLowUserSourceType {
	SOURCE_USER("source_user"), SOURCE_DEPARTMENT("source_department"), SOURCE_POSITION("source_position");
	
	private String code;
    private FLowUserSourceType(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static FLowUserSourceType fromCode(String code) {
    	if(code == null) {
    		return null;
    	}
    	
    	for(FLowUserSourceType t : FLowUserSourceType.values()) {
    		if(code.equalsIgnoreCase(t.getCode())) {
    			return t;
    		}
    	}

        return null;
    }
}
