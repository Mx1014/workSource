package com.everhomes.rest.activity;

public enum VideoSupportType {
    NO_SUPPORT((byte)0), VIDEO_ONLY((byte)1), VIDEO_BOTH((byte)2);
    
    private byte code;
    
    public byte getCode() {
        return this.code;
    }
    
    private VideoSupportType(byte code) {
        this.code = code;
    }
    
    public static VideoSupportType fromCode(Byte code) {
        if(code == null)
            return null;
        
        switch(code.byteValue()) {
        case 0 :
            return NO_SUPPORT;
        case 1 :
            return VIDEO_ONLY;
        case 2:
            return VIDEO_BOTH;
        }
        
        return null;
    }
}
