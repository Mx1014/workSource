// @formatter:off
package com.everhomes.rest.print;

/**
 * 
 * <ul>
 * <li>ENTERPRISE("enterprise"): 物业管理公司</li>
 * <li>COMMUNITY(community): 小区/园区</li>
 * </ul>
 *
 *  @author:dengs 2017年6月16日
 */
public enum PrintOwnerType {
	ENTERPRISE("enterprise"), COMMUNITY("community");
	
	private String code;
    private PrintOwnerType(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static PrintOwnerType fromCode(String code) {
    	if(code != null) {
    		PrintOwnerType[] values = PrintOwnerType.values();
            for(PrintOwnerType value : values) {
                if(value.getCode().equalsIgnoreCase(code)) {
                    return value;
                }
            }
        }
        
        return null;
    }
}
