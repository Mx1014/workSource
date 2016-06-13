package com.everhomes.rest.aclink;

public enum AesUserKeyType {
    NORMAL((byte)0), TEMP((byte)1), ADMIN((byte)1);
    
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
        case 1 :
            return TEMP;
        case 2 :
            return ADMIN;
        default :
            break;
        }
        
        return null;
    }
}
