package com.everhomes.rest.flow;

/**
 * <ul>
 * <li>department: 选择部门</li>
 * <li>position: 选择职位</li>
 * <li>manager: 选择经理</li>
 * <li>variable: 选择变量</li>
 * </ul>
 * @author janson
 *
 */
public enum FlowUserSelectionType {
	DEPARTMENT("department"),
    POSITION("position"),
    MANAGER("manager"),
    VARIABLE("variable"),
    ;
	
	private String code;
    private FlowUserSelectionType(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static FlowUserSelectionType fromCode(String code) {
    	if(code == null) {
    		return null;
    	}
    	
    	for(FlowUserSelectionType t : FlowUserSelectionType.values()) {
    		if(code.equalsIgnoreCase(t.getCode())) {
    			return t;
    		}
    	}

        return null;
    }
}
