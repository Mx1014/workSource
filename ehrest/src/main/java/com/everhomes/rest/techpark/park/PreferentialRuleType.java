package com.everhomes.rest.techpark.park;


/**
 * <ul>
 *  <li>PARKING: parking</li>
 * </ul>
 *
 */
public enum PreferentialRuleType {

	PARKING("parking");
	
	private String code;
    private PreferentialRuleType(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static PreferentialRuleType fromCode(String code) {
    	PreferentialRuleType[] values = PreferentialRuleType.values();
        for(PreferentialRuleType value : values) {
            if(value.code.equals(code)) {
                return value;
            }
        }
        
        return null;
    }
}
