package com.everhomes.techpark.expansion;

public enum ApplyEntrySourceType {
	
	BUILDING("building"), MARKET_ZONE("market_zone"), FOR_RENT("for_rent");
    
    private String code;
    private ApplyEntrySourceType(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static ApplyEntrySourceType fromType(String code) {
    	if(code == null) {
    		return null;
    	}
        
        if(code.equalsIgnoreCase(BUILDING.getCode())) {
        	return BUILDING;
        }

        if(code.equalsIgnoreCase(MARKET_ZONE.getCode())) {
        	return MARKET_ZONE;
        }
        return null;
    }
}
