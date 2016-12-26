package com.everhomes.rest.flow;

/**
 * <ul>
 * <li>step_none: 没有跳转</li>
 * <li>step_timeout: 节点超时</li>
 * <li>step_enter: 进入节点</li>
 * <li>step_leave: 离开节点</li>
 * 
 * </ul>
 * @author janson
 *
 */
public enum FlowActionStepType {
	STEP_NONE("step_none"), STEP_TIMEOUT("step_timeout"), STEP_ENTER("step_enter"), STEP_LEAVE("step_leave");
	
	private String code;
    private FlowActionStepType(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static FlowStepType fromCode(String code) {
    	if(code == null) {
    		return null;
    	}
    	
    	for(FlowStepType t : FlowStepType.values()) {
    		if(code.equalsIgnoreCase(t.getCode())) {
    			return t;
    		}
    	}

        return null;
    }
}
