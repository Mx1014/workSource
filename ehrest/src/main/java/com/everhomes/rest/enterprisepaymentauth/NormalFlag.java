package com.everhomes.rest.enterprisepaymentauth;

/**
 * <ul>
 * <li>NO(0): </li>
 * <li>YES(1): </li>
 * </ul>
 */
public enum NormalFlag {
   
	YES((byte)1),NO((byte)0);
    
    private byte code;
    private NormalFlag(Byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static NormalFlag fromCode(Byte code) {
        if (null == code) {
            return null;
        }
        for(NormalFlag t : NormalFlag.values()) {
            if (t.code == code) {
                return t;
            }
        }
        
        return null;
    }
}
