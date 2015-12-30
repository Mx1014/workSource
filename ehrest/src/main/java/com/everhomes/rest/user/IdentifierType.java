// @formatter:off
package com.everhomes.rest.user;

public enum IdentifierType {
    MOBILE((byte)0), EMAIL((byte)1);
    
    private byte code;
    private IdentifierType(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static IdentifierType fromString(String val) {
        if(val.equalsIgnoreCase("mobile"))
            return MOBILE;
        else if(val.equalsIgnoreCase("email"))
            return EMAIL;
        
        throw new RuntimeException("Invalid IdentifierType " + val);
    }
    
    public static IdentifierType fromCode(Byte code) {
        if(code == null)
            return null;
        
        switch(code.byteValue()) {
        case 0 :
            return MOBILE;
        case 1 :
            return EMAIL;
            
        default :
            break;
        }
        
        return null;
    }
}
