package com.everhomes.rest.flow;

/**
 * <ul>
 * <li>NODE_TRACKER: 节点的跟踪日志信息，包括评论</li>
 * <li>STEP_TRACKER: 节点的跳转信息</li>
 * </ul>
 * @author janson
 *
 */
public enum FlowLogType {
	BUTTON_FIRED("button_fired"), AUTO_STEP("auto_step"), NODE_ENTER("node_enter"), STEP_TRACKER("step_tracker"), NODE_TRACKER("node_tracker"), FLOW_SUPERVISOR("flow_supervisor"), NODE_REMIND("flow_remind");
	
	private String code;
    private FlowLogType(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static FlowLogType fromCode(String code) {
    	if(code == null) {
    		return null;
    	}
    	
    	for(FlowLogType t : FlowLogType.values()) {
    		if(code.equalsIgnoreCase(t.getCode())) {
    			return t;
    		}
    	}

        return null;
    }
}
