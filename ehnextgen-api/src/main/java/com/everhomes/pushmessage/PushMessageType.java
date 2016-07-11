package com.everhomes.pushmessage;

public enum PushMessageType {
    VERSION_UPGRADE((byte)2), NOTIFY((byte)1), NORMAL((byte)0);
    
    private byte code;
    private PushMessageType(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static PushMessageType fromCode(byte code) {
        for(PushMessageType t : PushMessageType.values()) {
            if (t.code == code) {
                return t;
            }
        }
        
        return null;
    }
}
