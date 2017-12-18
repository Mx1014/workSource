// @formatter:off
package com.everhomes.rest.reserve;

/**
 * <ul>
 * <li>INNER(1): 时长内</li>
 * <li>OUTER(2): 时长外</li>
 * </ul>
 */
public enum ReserveRuleDurationType {
	INNER((byte)1), OUTER((byte)2);

    private byte code;

    private ReserveRuleDurationType(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static ReserveRuleDurationType fromCode(Byte code) {
        if(code != null) {
            ReserveRuleDurationType[] values = ReserveRuleDurationType.values();
            for(ReserveRuleDurationType value : values) {
                if(value.code == code.byteValue()) {
                    return value;
                }
            }
        }
        
        return null;
    }
}
