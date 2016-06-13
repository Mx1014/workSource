package com.everhomes.rest.techpark.punch;

/**
 * <ul>
 * <li>LESSOREQUAL(0): 在**之前，小于等于</li>
 * <li>GREATEROREQUAL(1): 在**之后，大于等于</li>
 * </ul>
 */
public enum TimeCompareFlag {
    LESSOREQUAL((byte)0),GREATEROREQUAL((byte)1);
    
    private byte code;
    private TimeCompareFlag(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static TimeCompareFlag fromCode(byte code) {
        for(TimeCompareFlag t : TimeCompareFlag.values()) {
            if (t.code == code) {
                return t;
            }
        }
        
        return null;
    }
}
