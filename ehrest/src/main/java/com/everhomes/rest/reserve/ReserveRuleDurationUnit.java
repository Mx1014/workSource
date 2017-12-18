// @formatter:off
package com.everhomes.rest.reserve;

/**
 * <ul>
 * <li>HOUR(1): 小时</li>
 * <li>DAY(2): 天</li>
 * </ul>
 */
public enum ReserveRuleDurationUnit {
	HOUR((byte)1), DAY((byte)2);

    private byte code;

    private ReserveRuleDurationUnit(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static ReserveRuleDurationUnit fromCode(Byte code) {
        if(code != null) {
            ReserveRuleDurationUnit[] values = ReserveRuleDurationUnit.values();
            for(ReserveRuleDurationUnit value : values) {
                if(value.code == code.byteValue()) {
                    return value;
                }
            }
        }
        
        return null;
    }
}
