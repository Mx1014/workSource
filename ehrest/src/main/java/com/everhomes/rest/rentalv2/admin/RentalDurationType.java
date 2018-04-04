// @formatter:off
package com.everhomes.rest.rentalv2.admin;

/**
 * <ul>
 * <li>INNER(1): 时长内</li>
 * <li>OUTER(2): 时长外</li>
 * </ul>
 */
public enum RentalDurationType {
	INNER((byte)1), OUTER((byte)2);

    private byte code;

    private RentalDurationType(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static RentalDurationType fromCode(Byte code) {
        if(code != null) {
            RentalDurationType[] values = RentalDurationType.values();
            for(RentalDurationType value : values) {
                if(value.code == code.byteValue()) {
                    return value;
                }
            }
        }
        
        return null;
    }
}
