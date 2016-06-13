package com.everhomes.rest.user;

public enum UserLoginStatus {
    LOGGED_OFF((byte)0), LOGGED_IN((byte)1);
    
    private byte code;
    
    private UserLoginStatus(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static UserLoginStatus fromCode(Byte code) {
        if(code == null)
            return null;
        
        switch(code.byteValue()) {
        case 0:
            return LOGGED_OFF;
            
        case 1 :
            return LOGGED_IN;
            
        default :
            break;
        }
        
        return null;
    }
}
