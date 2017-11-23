// @formatter:off
package com.everhomes.rest.parking;

/**
 * <ul>
 * <li>NONE(0): 都不显示</li>
 * <li>CAR_NUM(1): 当前在场车数量</li>
 * <li>FREE_PLACE(2): 当前剩余车位</li>
 * </ul>
 */
public enum ParkingCurrentInfoType {
    NONE((byte)0), CAR_NUM((byte)1), FREE_PLACE((byte)2);

    private byte code;


	private ParkingCurrentInfoType(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static ParkingCurrentInfoType fromCode(Byte code) {
        if(code != null) {
            ParkingCurrentInfoType[] values = ParkingCurrentInfoType.values();
            for(ParkingCurrentInfoType value : values) {
                if(value.code == code.byteValue()) {
                    return value;
                }
            }
        }
        
        return null;
    }
}
