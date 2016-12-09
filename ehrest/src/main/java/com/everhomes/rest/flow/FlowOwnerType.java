package com.everhomes.rest.flow;

/**
 * <ul>
 * <li>enterprise: 属于一家企业</li>
 * <li>enterprise: 属于一个部门</li>
 * <li>community: 属于一个园区</li>
 * </ul>
 * @author janson
 *
 */
public enum FlowOwnerType {
	ENTERPRISE("enterprise"), DEPARTMENT("department"), COMMUNITY("community");
	
	private String code;
    private FlowOwnerType(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static FlowOwnerType fromCode(String code) {
    	if(code == null) {
    		return null;
    	}
    	
    	for(FlowOwnerType t : FlowOwnerType.values()) {
    		if(code.equalsIgnoreCase(t.getCode())) {
    			return t;
    		}
    	}

        return null;
    }
}
