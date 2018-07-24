package com.everhomes.rest.aclink;

/**
 * <ul>
 * <li>NO((byte)0): 否</li>
 * <li>YES((byte)1):是</li>
 * </ul>
 *
 */
public enum DoorAuthEnableAmount {
	NO((byte)0), YES((byte)1);
    
    private byte code;
    
    public byte getCode() {
        return this.code;
    }
    
    private DoorAuthEnableAmount(byte code) {
        this.code = code;
    }
    
    public static DoorAuthEnableAmount fromCode(Byte code) {
        if(code == null)
            return null;
        
        switch(code.byteValue()) {
        case 0 :
            return YES;
            
        case 1 :
            return NO;
        }
        
        return null;
    }
}
