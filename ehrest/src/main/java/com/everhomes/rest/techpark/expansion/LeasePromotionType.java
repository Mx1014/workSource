package com.everhomes.rest.techpark.expansion;

/**
 * ORDINARY("building"):园区入驻
 * OFFICE_CUBICLE("office_cubicle"): 工位预定
 * */
public enum LeasePromotionType {
	
	ORDINARY("building"),OFFICE_CUBICLE("office_cubicle");
	
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
