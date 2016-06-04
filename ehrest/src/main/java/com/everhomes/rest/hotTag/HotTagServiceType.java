package com.everhomes.rest.hotTag;


/**
 *<ul>
 *<li>ACTIVITY("activity"):活动</li>
 *</ul>
 */
public enum HotTagServiceType {

	ACTIVITY("activity");
    
    private String code;
    private HotTagServiceType(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static HotTagServiceType fromCode(String code) {
    	if(code == null) {
    		return null;
    	}

        if(code.equalsIgnoreCase(ACTIVITY.getCode())) {
        	return ACTIVITY;
        }
        
        return null;
    }

}
