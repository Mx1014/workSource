// @formatter:off
package com.everhomes.rest.reserve;

/**
 * <ul>
 * <li>CUSTOM(1): 自定义</li>
 * <li>FULL(2): 原价,全额</li>
 * </ul>
 */
public enum ReserveOrderHandleRuleType {
	CUSTOM((byte)1), FULL((byte)2);

    private byte code;

    private ReserveOrderHandleRuleType(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static ReserveOrderHandleRuleType fromCode(Byte code) {
        if(code != null) {
            ReserveOrderHandleRuleType[] values = ReserveOrderHandleRuleType.values();
            for(ReserveOrderHandleRuleType value : values) {
                if(value.code == code.byteValue()) {
                    return value;
                }
            }
        }
        
        return null;
    }
}
