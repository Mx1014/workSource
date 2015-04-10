// @formatter:off
package com.everhomes.group;

public enum GroupPrivacy {

    PUBLIC((byte)0), PRIVATE((byte)1);
    
    private byte code;
    
    private GroupPrivacy(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static GroupPrivacy fromCode(Byte code) {
        if(code == null)
            return null;
        
        switch(code.byteValue()) {
        case 0 :
            return PUBLIC;
            
        case 1 :
            return PRIVATE;
            
        default :
            break;
        }
        
        return null;
    }
}
