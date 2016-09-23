// @formatter:off
package com.everhomes.rest.parking;

/**
 * <ul>
 * <li>MONTHLY(1): 月卡缴费</li>
 * <li>TEMPORARY(2): 临时缴费</li>
 * </ul>
 */
public enum ParkingRechargeType {
	MONTHLY((byte)1), TEMPORARY((byte)2);
    
    private byte code;
    
    private ParkingRechargeType(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static ParkingRechargeType fromCode(Byte code) {
        if(code != null) {
            ParkingRechargeType[] values = ParkingRechargeType.values();
            for(ParkingRechargeType value : values) {
                if(value.code == code.byteValue()) {
                    return value;
                }
            }
        }
        
        return null;
    }
}
