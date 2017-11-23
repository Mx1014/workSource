// @formatter:off
package com.everhomes.rest.parking;

/**
 * <ul>
 *     月卡过期时充值，支持类型
 * <li>ALL((byte)1): 整月</li>
 * <li>ACTUAL((byte)2): 实际天数</li>
 * </ul>
 */
public enum ParkingCardExpiredRechargeType {
    ALL((byte)1), ACTUAL((byte)2);
    
    private byte code;
    
    private ParkingCardExpiredRechargeType(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static ParkingCardExpiredRechargeType fromCode(Byte code) {
        if(code != null) {
            ParkingCardExpiredRechargeType[] values = ParkingCardExpiredRechargeType.values();
            for(ParkingCardExpiredRechargeType value : values) {
                if(value.code == code.byteValue()) {
                    return value;
                }
            }
        }
        
        return null;
    }
}
