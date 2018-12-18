package com.everhomes.rest.officecubicle;

/**
 * <ul>
 * <li>NONEED(0): </li>
 * <li>NEED(1): </li>
 * </ul>
 */
public enum NormalFlag {
   
	NEED((byte)1),NONEED((byte)0);
    
    private byte code;
    private NormalFlag(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static NormalFlag fromCode(byte code) {
        for(NormalFlag t : NormalFlag.values()) {
            if (t.code == code) {
                return t;
            }
        }
        
        return null;
    }
}
