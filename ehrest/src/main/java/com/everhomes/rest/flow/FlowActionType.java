package com.everhomes.rest.flow;

/**
 * <ul>
 * <li>message: 发消息的动作</li>
 * <li>sms: 发短信的动作</li>
 * <li>tick_message: 定时发多次消息的动作</li>
 * <li>tick_sms: 定时发多次短信的动作</li>
 * <li>track: 跟踪动作</li>
 * <li>enter_script: 前置脚本</li>
 * <li></li>
 * <li></li>
 * 
 * </ul>
 * @author janson
 *
 */
public enum FlowActionType {
	MESSAGE("message"), SMS("sms"), TICK_MESSAGE("tick_message"), TICK_SMS("tick_sms"), TRACK("track"), ENTER_SCRIPT("enter_script");
	
	private String code;
    private FlowActionType(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static FlowActionType fromCode(String code) {
    	if(code == null) {
    		return null;
    	}
    	
    	for(FlowActionType t : FlowActionType.values()) {
    		if(code.equalsIgnoreCase(t.getCode())) {
    			return t;
    		}
    	}

        return null;
    }
}
