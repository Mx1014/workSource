// @formatter:off
package com.everhomes.rest.messaging;

/**
 * <ul>
 *     <li>NOTICE("NOTICE"): 通知类型</li>
 *     <li>MESSAGE("MESSAGE"): 会话类型</li>
 * </ul>
 */
public enum UserMessageType {

    NOTICE("NOTICE"), MESSAGE("MESSAGE");

    private String code;

    UserMessageType(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static UserMessageType fromCode(String code) {
        for(UserMessageType t : UserMessageType.values()) {
            if (t.code.equals(code)) {
                return t;
            }
        }
        return null;
    }
}
