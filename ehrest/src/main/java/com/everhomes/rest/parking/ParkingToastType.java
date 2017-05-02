// @formatter:off
package com.everhomes.rest.parking;

/**
 * <ul>
 * <li>CARD_EXPIRED(1): 月卡过期</li>
 * <li>NOT_CARD_USER(2): 非月卡用户</li>
 * </ul>
 */
public enum ParkingToastType {
    CARD_EXPIRED((byte)1), NOT_CARD_USER((byte)2);

    private byte code;

    private ParkingToastType(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static ParkingToastType fromCode(Byte code) {
        if(code != null) {
            ParkingToastType[] values = ParkingToastType.values();
            for(ParkingToastType value : values) {
                if(value.code == code.byteValue()) {
                    return value;
                }
            }
        }
        
        return null;
    }
}
