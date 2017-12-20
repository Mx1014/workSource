// @formatter:off
package com.everhomes.rest.reserve;

/**
 * <ul>
 * <li>CUSTOM(1): 自定义</li>
 * <li>FULL(2): 原价,全额</li>
 * </ul>
 */
public enum ReserveOrderHandleType {
	CUSTOM((byte)1), FULL((byte)2);

    private byte code;

    private ReserveOrderHandleType(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static ReserveOrderHandleType fromCode(Byte code) {
        if(code != null) {
            ReserveOrderHandleType[] values = ReserveOrderHandleType.values();
            for(ReserveOrderHandleType value : values) {
                if(value.code == code.byteValue()) {
                    return value;
                }
            }
        }
        
        return null;
    }
}
