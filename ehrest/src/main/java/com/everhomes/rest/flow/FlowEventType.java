package com.everhomes.rest.flow;

/**
 * <ul> 暂时用不到
 * <li>step_start: 进入节点的日志</li>
 * <li>step_timeout: 节点超时的日志</li>
 * <li>button_fired: 按钮响应的日志</li>
 * </ul>
 * @author janson
 *
 */
public enum FlowEventType {
	STEP_START("step_start"), STEP_TIMEOUT("step_timeout"), BUTTON_FIRED("button_fired");
	
	private String code;
    private FlowEventType(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static FlowEventType fromCode(String code) {
    	if(code == null) {
    		return null;
    	}
    	
    	for(FlowEventType t : FlowEventType.values()) {
    		if(code.equalsIgnoreCase(t.getCode())) {
    			return t;
    		}
    	}

        return null;
    }
}
