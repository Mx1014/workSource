package com.everhomes.rest.techpark.expansion;

/**
 * <ul> 租金单位
 * <li>MONTH_UNIT：月</li>
 * </ul>
 * */
public enum LeasePromotionUnit {

	MONTH_UNIT("MONTH_UNIT", "元/㎡/月");

    private String code;
    private String description;

    private LeasePromotionUnit(String code, String description) {
        this.code = code;
        this.description = description;
    }
    
    public String getCode() {
        return this.code;
    }

    public String getDescription() {
        return description;
    }

    public static LeasePromotionUnit fromType(String code) {
    	if(code == null) {
    		return null;
    	}
        
        if(code.equalsIgnoreCase(MONTH_UNIT.getCode())) {
        	return MONTH_UNIT;
        }
        return null;
    }
}
