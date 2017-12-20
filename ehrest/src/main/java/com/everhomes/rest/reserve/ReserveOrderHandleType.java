// @formatter:off
package com.everhomes.rest.reserve;

/**
 * <ul>
 * <li>REFUND(1): 退款</li>
 * <li>OVERTIME(2): 超时加收</li>
 * </ul>
 */
public enum ReserveOrderHandleType {
	REFUND((byte)1), OVERTIME((byte)2);

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
