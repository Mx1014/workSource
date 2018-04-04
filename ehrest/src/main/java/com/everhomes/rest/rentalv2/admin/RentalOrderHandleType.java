// @formatter:off
package com.everhomes.rest.rentalv2.admin;

/**
 * <ul>
 * <li>REFUND(1): 退款</li>
 * <li>OVERTIME(2): 超时加收</li>
 * </ul>
 */
public enum RentalOrderHandleType {
	REFUND((byte)1), OVERTIME((byte)2);

    private byte code;

    private RentalOrderHandleType(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static RentalOrderHandleType fromCode(Byte code) {
        if(code != null) {
            RentalOrderHandleType[] values = RentalOrderHandleType.values();
            for(RentalOrderHandleType value : values) {
                if(value.code == code.byteValue()) {
                    return value;
                }
            }
        }
        
        return null;
    }
}
