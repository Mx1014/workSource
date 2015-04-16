// @formatter:off
package com.everhomes.group;

public enum GroupOpType {
    REQUEST_ADMIN_ROLE((byte)1), INVITE_ADMIN_ROLE((byte)2);
    
    private byte code;
    private GroupOpType(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static GroupOpType fromCode(Byte code) {
        if(code == null)
            return null;
        
        switch(code.byteValue()) {
        case 1:
            return REQUEST_ADMIN_ROLE;
        case 2:
            return INVITE_ADMIN_ROLE;
        default :
            break;
        }
        
        return null;
    }
}
