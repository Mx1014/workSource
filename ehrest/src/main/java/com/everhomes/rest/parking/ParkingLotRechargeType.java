// @formatter:off
package com.everhomes.rest.parking;

/**
 * <ul>
 * <li>ALL((byte)1): 整月</li>
 * <li>ACTUAL((byte)2): 实际天数</li>
 * </ul>
 */
public enum ParkingLotRechargeType {
    ALL((byte)1), ACTUAL((byte)2);
    
    private byte code;
    
    private ParkingLotRechargeType(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static ParkingLotRechargeType fromCode(Byte code) {
        if(code != null) {
            ParkingLotRechargeType[] values = ParkingLotRechargeType.values();
            for(ParkingLotRechargeType value : values) {
                if(value.code == code.byteValue()) {
                    return value;
                }
            }
        }
        
        return null;
    }
}
