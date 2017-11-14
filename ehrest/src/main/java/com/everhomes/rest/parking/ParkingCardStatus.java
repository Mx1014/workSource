// @formatter:off
package com.everhomes.rest.parking;

/**
 * <ul>停车充值 卡状态
 * <li>NORMAL(1): 正常</li>
 * <li>EXPIRED(2): 过期, 不可充值</li>
 * <li>SUPPORT_EXPIRED_RECHARGE(3): 过期, 可充值</li>
 * </ul>
 */
public enum ParkingCardStatus {
    NORMAL((byte)1), EXPIRED((byte)2), SUPPORT_EXPIRED_RECHARGE((byte)3);

    private byte code;

    private ParkingCardStatus(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static ParkingCardStatus fromCode(Byte code) {
        if(code != null) {
            ParkingCardStatus[] values = ParkingCardStatus.values();
            for(ParkingCardStatus value : values) {
                if(value.code == code.byteValue()) {
                    return value;
                }
            }
        }
        
        return null;
    }
}
