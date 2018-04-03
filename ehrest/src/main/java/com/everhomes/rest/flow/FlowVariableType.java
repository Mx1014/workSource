package com.everhomes.rest.flow;

/**
 * <ul> 变量类型 
 * <li>text: 文本变量，不再使用</li>
 * <li>node_user: 节点用户选择变量，不再使用，为了兼容旧的继续留着</li>
 * 
 * <li>node_user_processor: 配置节点处理人</li>
 * <li>node_user_button_msg: 配置按钮消息目标人员</li>
 * <li>node_user_remind: 消息提醒里面的目标人员</li>
 * 
 * <li>text_button_msg: 按钮消息里面的文本变量</li>
 * <li>text_remind: 按钮消息里面的文本变量</li>
 * <li>text_tracker: 跟踪里面的文本变量</li>
 * 
 * </ul>
 * @author janson
 *
 */
public enum FlowVariableType {
	TEXT("text"), NODE_USER("node_user") //新版本这两个值已经不再使用
	, NODE_USER_PROCESSOR("node_user_processor") //配置节点处理人
	, NODE_USER_BUTTON("node_user_button_msg")			//配置按钮消息目标人员
	, NODE_USER_REMIND("node_user_remind")					//消息提醒
	, TEXT_BUTTON("text_button_msg") 						//按钮里面的文本对象
	, TEXT_REMIND("text_remind")								//消息提醒里面的按钮对象
	, TEXT_TRACKER("text_tracker") 							//跟踪里面的按钮消息对象
	;
	
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
