// @formatter:off
package com.everhomes.rest.rentalv2.admin;

/**
 * <ul>
 * <li>CUSTOM(1): 自定义</li>
 * <li>FULL(2): 原价,全额</li>
 * <li>NONE(0): 不支持退款, 不加收</li>
 * </ul>
 */
public enum RentalOrderStrategy {
    NONE((byte)0), CUSTOM((byte)1), FULL((byte)2);

    private byte code;

    private RentalOrderStrategy(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static RentalOrderStrategy fromCode(Byte code) {
        if(code != null) {
            RentalOrderStrategy[] values = RentalOrderStrategy.values();
            for(RentalOrderStrategy value : values) {
                if(value.code == code.byteValue()) {
                    return value;
                }
            }
        }
        
        return null;
    }
}
