// @formatter:off
package com.everhomes.rest.pkg;

public enum DevicePlatform {
	ANDRIOD((byte)1), IOS((byte)2);
    
    private byte code;
    
    private DevicePlatform(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static DevicePlatform fromCode(Byte code) {
        if(code == null)
            return null;
        
        switch(code.byteValue()) {
        case 1:
            return ANDRIOD;
            
        case 2:
            return IOS;
            
        default :
            assert(false);
            break;
        }
        return null;
    }
}
