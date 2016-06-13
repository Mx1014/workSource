// @formatter:off
package com.everhomes.rest.link;

/**
 * <ul>
 * <li>FORWARD("forward"): 转发</li>
 * <li>CREATE("create"): 新创建</li>
 * </ul>
 */
public enum LinkContentType {
    FORWARD("forward"), CREATE("create");
    
    private String code;
    private LinkContentType(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static LinkContentType fromCode(String code) {
    	if(code == null) {
    		return null;
    	}
        
        if(code.equalsIgnoreCase(FORWARD.getCode())) {
        	return FORWARD;
        }

        if(code.equalsIgnoreCase(CREATE.getCode())) {
        	return CREATE;
        }

        return null;
    }
}
