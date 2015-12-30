package com.everhomes.rest.techpark.expansion;

/**
 * ORDINARY(1):普通招租
 * */
public enum LeasePromotionType {
	
	ORDINARY("building");
	
    private String code;
    private LeasePromotionType(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static LeasePromotionType fromType(String code) {
    	if(code == null) {
    		return null;
    	}
        
        if(code.equalsIgnoreCase(ORDINARY.getCode())) {
        	return ORDINARY;
        }
        return null;
    }
}
