package com.everhomes.rest.flow;

/**
 * <ul> 超时类型
 * <li>step_timeout: 节点动作超时</li>
 * <li>remind_timeout: 节点消息通知超时</li>
 * <li>message_timeout: 消息延迟发送</li>
 * </ul>
 * @author janson
 *
 */
public enum FlowTimeoutType {
	STEP_TIMEOUT("step_timeout"), REMIND_TIMEOUT("remind_timeout"), MESSAGE_TIMEOUT("message_timeout"), SMS_TIMEOUT("sms_timeout");
	
	private String code;
    private FlowTimeoutType(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static FlowTimeoutType fromCode(String code) {
    	if(code == null) {
    		return null;
    	}
    	
    	for(FlowTimeoutType t : FlowTimeoutType.values()) {
    		if(code.equalsIgnoreCase(t.getCode())) {
    			return t;
    		}
    	}

        return null;
    }
}
