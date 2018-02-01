package com.everhomes.rest.socialSecurity;

/**
 * <ul>
 * <li>NORMAL(0): 普普通通</li>
 * <li>INCRE(1): 增员</li>
 * <li>DECRE(-1): 减员</li>
 * </ul>
 */
public enum InOutFlag {

	INCRE((byte)1),NORMAL((byte)0),DECRE((byte)-1);

    private byte code;
    private InOutFlag(Byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static InOutFlag fromCode(Byte code) {
        if (null == code) {
            return null;
        }
        for(InOutFlag t : InOutFlag.values()) {
            if (t.code == code) {
                return t;
            }
        }
        
        return null;
    }
}
