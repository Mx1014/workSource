package com.everhomes.rest.flow;

/**
 * <ul>
 * <li>flow: 工作流</li>
 * <li>flow_node: 工作流节点</li>
 * <li>flow_action: 工作流动作</li>
 * <li>flow_button: 工作流按钮</li>
 * <li>flow_selection: 工作流的用户选择项</li>
 * <li>flow_user: 工作流的某个选择用户</li>
 * 
 * </ul>
 * @author janson
 *
 */
public enum FlowEntityType {
	FLOW("flow"),
    FLOW_NODE("flow_node"),
    FLOW_ACTION("flow_action"),
    FLOW_BUTTON("flow_button"),
    FLOW_SELECTION("flow_selection"),
    FLOW_USER("flow_user"),
    FLOW_EVALUATE("flow_evaluate"),
    ;
	
	private String code;
    private FlowEntityType(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static FlowEntityType fromCode(String code) {
    	if(code == null) {
    		return null;
    	}
    	
    	for(FlowEntityType t : FlowEntityType.values()) {
    		if(code.equalsIgnoreCase(t.getCode())) {
    			return t;
    		}
    	}

        return null;
    }
}
