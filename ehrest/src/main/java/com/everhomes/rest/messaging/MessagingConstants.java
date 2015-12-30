package com.everhomes.rest.messaging;

/**
 * Message routing service
 * <ul>
 * <li>MSG_FLAG_STORED: 存储转发 </li>
 * <li>MSG_FLAG_PUSH_ENABLED: 需推送消息</li>
 * <li>MSG_FLAG_VOICE_ALERT: 语音消息</li>
 * </ul>
 * 
 */
public enum MessagingConstants {
    MSG_FLAG_STORED(0x1),
    MSG_FLAG_PUSH_ENABLED(0x2),
    MSG_FLAG_STORED_PUSH(0x3),
    MSG_FLAG_VOICE_ALERT(0x4),
    MSG_FLAG_REFLECT_BACK(0x8);

    private int code;
    private MessagingConstants(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

    public static MessagingConstants fromCode(Integer code) {
        if(code == null)
            return null;

        switch(code.intValue()) {
            case 1:
                return MSG_FLAG_STORED;
                
            case 2 :
                return MSG_FLAG_PUSH_ENABLED;
                
            case 3:
                return MSG_FLAG_STORED_PUSH;
                
            case 4 :
                return MSG_FLAG_VOICE_ALERT;

            case 8 :
                return MSG_FLAG_REFLECT_BACK;
                        
            default :
                break;
        }

        return null;
    }
}
