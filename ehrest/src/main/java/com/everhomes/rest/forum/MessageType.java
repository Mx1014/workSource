// @formatter:off
package com.everhomes.rest.forum;

/**
 * <p>消息类型</p>
 * <ul>
 * <li>PUSH("push"): 文本</li>
 * <li>SMS("sms"): 图片</li>
 * <li>COMMENT("comment"): 音频</li>
 * </ul>
 */
public enum MessageType {
    PUSH("push"), SMS("sms"), COMMENT("comment");
    
    private String code;
    private MessageType(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static MessageType fromCode(String code) {
        if(code != null) {
        	MessageType[] values = MessageType.values();
            for(MessageType value : values) {
                if(code.equals(value.code)) {
                    return value;
                }
            }
        }
        
        return null;
    }
}
