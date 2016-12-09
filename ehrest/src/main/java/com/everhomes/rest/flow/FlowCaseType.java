package com.everhomes.rest.flow;

/**
 * <ul>
 * <li>inner: 工作流内部的 Case</li>
 * </ul>
 * @author janson
 *
 */
public enum FlowCaseType {
	INNER("inner");
	
	private String code;
    private FlowCaseType(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static FlowCaseType fromCode(String code) {
    	if(code == null) {
    		return null;
    	}
    	
    	for(FlowCaseType t : FlowCaseType.values()) {
    		if(code.equalsIgnoreCase(t.getCode())) {
    			return t;
    		}
    	}

        return null;
    }
}
