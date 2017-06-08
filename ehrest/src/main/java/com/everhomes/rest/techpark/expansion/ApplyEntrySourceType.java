package com.everhomes.rest.techpark.expansion;

/**
 * BUILDING("building") 来自楼栋列表申请入住
 * MARKET_ZONE("market_zone") 来自创客空间申请入住
 * FOR_RENT("for_rent") 来自招租信息申请入住
 * OFFICE_CUBICLE("office_cubicle") 来自工位预定申请入住
 */
public enum ApplyEntrySourceType {
	
	BUILDING("building"), MARKET_ZONE("market_zone"), FOR_RENT("for_rent"),OFFICE_CUBICLE("office_cubicle");
    
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
