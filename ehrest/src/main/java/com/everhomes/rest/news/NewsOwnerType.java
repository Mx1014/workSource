// @formatter:off
package com.everhomes.rest.news;

/**
 * 
 * <ul>
 * <li>ORGANIZATION: 组织</li>
 * <li>COMMUNITY: 项目</li>
 * </ul>
 */
public enum NewsOwnerType {
	ORGANIZATION("organization"),
	COMMUNITY("EhCommunities");
	private String code;
	private NewsOwnerType(String code) {
		this.code = code;
	}
	public String getCode() {
        return this.code;
    }
	public static NewsOwnerType fromCode(String code) {
    	if(code == null) {
    		return null;
    	}
        
        if(code.equalsIgnoreCase(ORGANIZATION.getCode())) {
        	return ORGANIZATION;
        }

        return null;
    }
}
