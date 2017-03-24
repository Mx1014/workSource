package com.everhomes.flow;

import com.everhomes.rest.flow.FlowCaseType;

/**
 * <ul>
 * <li>STEP_SYNC_PROCESS: 同步执行，节点可能跳转</li>
 * <li>NO_STEP_PROCESS: 同步执行，节点肯定不跳转</li>
 * <li>STEP_ASYNC_TIMEOUT: 超时任务执行中，节点已经跳转成功，日志已经完成，一般发消息会在此情况</li>
 * </ul>
 * @author janson
 *
 */
public enum FlowCaseStateStackType {
	STEP_SYNC_PROCESS("STEP_SYNC_PROCESS"), NO_STEP_PROCESS("NO_STEP_PROCESS"), STEP_ASYNC_TIMEOUT("STEP_ASYNC_TIMEOUT")
		, TRACKER_ACTION("TRACKER_ACTION");
	
	private String code;
    private FlowCaseStateStackType(String code) {
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
