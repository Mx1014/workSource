package com.everhomes.rest.flow;

/**
 * <ul> 变量类型 
 * <li>text: 文本变量</li>
 * <li>node_user: 节点用户选择变量</li>
 * </ul>
 * @author janson
 *
 */
public enum FlowVariableType {
	TEXT("text"), NODE_USER("node_user");
	
	private String code;
    private FlowVariableType(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static FlowVariableType fromCode(String code) {
    	if(code == null) {
    		return null;
    	}
    	
    	for(FlowVariableType t : FlowVariableType.values()) {
    		if(code.equalsIgnoreCase(t.getCode())) {
    			return t;
    		}
    	}

        return null;
    }
}
