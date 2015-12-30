// @formatter:off
package com.everhomes.rest.pkg;

public enum PackageEdition {
    USER_EDITION((byte)1), BUSINESS_EDITION((byte)2), COMMUNITY_EDITION((byte)3);
    
    private byte code;
    
    private PackageEdition(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static PackageEdition fromCode(Byte code) {
        if(code == null)
            return null;
        
        switch(code.byteValue()) {
        case 1:
            return USER_EDITION;
            
        case 2:
            return BUSINESS_EDITION;
            
        case 3:
            return COMMUNITY_EDITION;
            
        default :
            assert(false);
            break;
        }
        return null;
    }
}
