package com.everhomes.rest.aclink;

/**
 * <ul>
 * <li>FOREVER: 永久授权</li>
 * <li>TEMPERATE: 临时有效期授权</li>
 * <li>LINGLING_VISITOR: 令令访客授权</li>
 * </ul>
 * @author janson
 *
 */
public enum DoorAuthType {
    FOREVER((byte)0), TEMPERATE((byte)1), LINGLING_VISITOR((byte)2), ZUOLIN_VISITOR((byte)3), HUARUN_VISITOR((byte)4);
    private byte code;
    
    private DoorAuthType(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static DoorAuthType fromCode(Byte code) {
        if(code == null)
            return null;
        
        switch(code.byteValue()) {
        case 0 :
            return FOREVER;
        case 1 :
            return TEMPERATE;
        case 2 :
            return LINGLING_VISITOR;
        case 3 :
            return ZUOLIN_VISITOR;
        case 4 :
        		return HUARUN_VISITOR;
        default :
            break;
        }
        
        return null;
    }
}
