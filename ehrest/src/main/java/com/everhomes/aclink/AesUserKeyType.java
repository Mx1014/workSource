package com.everhomes.aclink;

public enum AesUserKeyType {
    NORMAL((byte)0);
    
    private byte code;
    
    private AesUserKeyType(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static AesUserKeyType fromCode(Byte code) {
        if(code == null)
            return null;
        
        switch(code.byteValue()) {
        case 0 :
            return NORMAL;
        default :
            break;
        }
        
        return null;
    }
}
