package com.everhomes.rest.flow;

/**
 * <ul>
 * <li>APPLIER(0): 我的申请</li>
 * <li>TODO_LIST(1): 我的代办</li>
 * <li>DONE_LIST(2): 已处理的任务</li>
 * <li>SUPERVISOR(3): 我的督办</li>
 * <li>ADMIN(4): 管理员操作</li>
 * </ul>
 * @author janson
 *
 */
public enum FlowCaseSearchType {
	APPLIER((byte)0), TODO_LIST((byte)1), DONE_LIST((byte)2), SUPERVISOR((byte)3), ADMIN((byte)4);
	private byte code;
    
    public byte getCode() {
        return this.code;
    }
    
    private FlowCaseSearchType(byte code) {
        this.code = code;
    }
    
    public static FlowCaseSearchType fromCode(Byte code) {
        if(code == null)
            return null;
        
    	for(FlowCaseSearchType t : FlowCaseSearchType.values()) {
    		if(code.equals(t.getCode())) {
    			return t;
    		}
    	}
        
        return null;
    }
}
