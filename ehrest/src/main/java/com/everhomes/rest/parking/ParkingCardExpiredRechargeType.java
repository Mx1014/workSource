// @formatter:off
package com.everhomes.rest.parking;

/**
 * <ul>
 * <li>ALL((byte)1): 整月</li>
 * <li>ACTUAL((byte)2): 实际天数</li>
 * </ul>
 */
public enum ParkingLotConfigRechargeType {
    ALL((byte)1), ACTUAL((byte)2);
    
    private byte code;
    
    private ParkingLotConfigRechargeType(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static ParkingLotConfigRechargeType fromCode(Byte code) {
        if(code != null) {
            ParkingLotConfigRechargeType[] values = ParkingLotConfigRechargeType.values();
            for(ParkingLotConfigRechargeType value : values) {
                if(value.code == code.byteValue()) {
                    return value;
                }
            }
        }
        
        return null;
    }
}
