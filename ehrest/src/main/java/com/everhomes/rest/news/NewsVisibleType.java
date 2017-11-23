// @formatter:off
package com.everhomes.rest.news;

/**
 * 
 * <ul>
 * <li>ALL: 全部</li>
 * <li>PART: 部分</li>
 * </ul>
 */
public enum NewsVisibleType {
	ALL("ALL"),
	PART("PART");
	private String code;
	private NewsVisibleType(String code) {
		this.code = code;
	}
	public String getCode() {
        return this.code;
    }
	public static NewsVisibleType fromCode(String code) {
    	if(code == null) {
    		return null;
    	}
        
        if(code.equalsIgnoreCase(ALL.getCode())) {
        	return ALL;
        }
		if(code.equalsIgnoreCase(PART.getCode())) {
			return PART;
		}
        return null;
    }
}
