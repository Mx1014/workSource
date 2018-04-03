package com.everhomes.rest.techpark.expansion;

/**
 * <ul>
 * <li>ORDINARY("1"):园区入驻-历史遗留问题</li>
 * <li>OFFICE_CUBICLE("office_cubicle"): 工位预定</li>
 * <li>BUILDING("building"): 楼栋出租</li>
 * </ul>
 * */
public enum LeasePromotionType {
	
	ORDINARY("1"),OFFICE_CUBICLE("office_cubicle"),BUILDING("building");
	
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
