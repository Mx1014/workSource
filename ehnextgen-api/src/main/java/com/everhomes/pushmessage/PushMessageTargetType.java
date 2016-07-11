package com.everhomes.pushmessage;

public enum PushMessageTargetType {
    CITY((byte)3), COMMUNITY((byte)2), FAMILY((byte)1), USER((byte)0);
    
    private byte code;
    private PushMessageTargetType(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static PushMessageTargetType fromCode(byte code) {
        for(PushMessageTargetType t : PushMessageTargetType.values()) {
            if (t.code == code) {
                return t;
            }
        }
        
        return null;
    }
}
