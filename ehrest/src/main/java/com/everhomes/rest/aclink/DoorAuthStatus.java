package com.everhomes.rest.aclink;

/**
 * <ul> 授权状态
 * <li>0: 已失效</li>
 * <li>1: 未失效</li>
 * </ul>
 * @author janson
 *
 */
public enum DoorAuthStatus {
    INVALID((byte)0), VALID((byte)1);
    
    private byte code;
    
    private DoorAuthStatus(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static DoorAuthStatus fromCode(Byte code) {
        if(code == null)
            return null;
        
        switch(code.byteValue()) {
        case 0 :
            return INVALID;
        case 1 :
            return VALID;
        default :
            break;
        }
        
        return null;
    }
}
