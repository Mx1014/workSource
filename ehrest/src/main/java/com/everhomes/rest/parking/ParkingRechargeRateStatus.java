// @formatter:off
package com.everhomes.rest.parking;

/**
 * <ul>停车充值订单状态
 * <li>INACTIVE(0): 无效</li>
 * <li>WAITING(1): 未支付</li>
 * <li>ACTIVE(2): 已支付</li>
 * </ul>
 */
public enum ParkingRechargeRateStatus {
    INACTIVE((byte)0), WAITING((byte)1), ACTIVE((byte)2);
    
    private byte code;
    
    private ParkingRechargeRateStatus(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static ParkingRechargeRateStatus fromCode(Byte code) {
        if(code != null) {
            ParkingRechargeRateStatus[] values = ParkingRechargeRateStatus.values();
            for(ParkingRechargeRateStatus value : values) {
                if(value.code == code.byteValue()) {
                    return value;
                }
            }
        }
        
        return null;
    }
}
