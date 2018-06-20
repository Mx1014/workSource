package com.everhomes.servicehotline;

/**
 * <ul>
 * <li>NO(0): </li>
 * <li>YES(1): </li>
 * </ul>
 */
public enum HotlineFlag {
   
	YES((byte)1),NO((byte)0);
    
    private byte code;
    private HotlineFlag(Byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static HotlineFlag fromCode(Byte code) {
        if (null == code) {
            return null;
        }
        for(HotlineFlag t : HotlineFlag.values()) {
            if (t.code == code) {
                return t;
            }
        }
        
        return null;
    }
}
