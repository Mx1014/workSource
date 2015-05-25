package com.everhomes.messaging;

public enum MessagingConstants {
    MSG_FLAG_STORED(0x1),
    MSG_FLAG_PUSH_ENABLED(0x2),
    MSG_FLAG_VOICE_ALERT(0x4);

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
            case 3 :
                return MSG_FLAG_VOICE_ALERT;

            default :
                break;
        }

        return null;
    }
}
