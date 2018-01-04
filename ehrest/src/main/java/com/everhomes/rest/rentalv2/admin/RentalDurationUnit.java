// @formatter:off
package com.everhomes.rest.rentalv2.admin;

/**
 * <ul>
 * <li>HOUR(1): 小时</li>
 * </ul>
 */
public enum RentalDurationUnit {
	HOUR((byte)1);

    private byte code;

    private RentalDurationUnit(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static RentalDurationUnit fromCode(Byte code) {
        if(code != null) {
            RentalDurationUnit[] values = RentalDurationUnit.values();
            for(RentalDurationUnit value : values) {
                if(value.code == code.byteValue()) {
                    return value;
                }
            }
        }
        
        return null;
    }
}
