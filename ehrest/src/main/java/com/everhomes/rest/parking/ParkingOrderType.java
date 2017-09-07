// @formatter:off
package com.everhomes.rest.parking;

/**
 * <ul>
 * <li>RECHARGE(1): 月卡缴费或者临时缴费订单</li>
 * <li>OPEN_CARD(2): 开卡支付订单</li>
 * </ul>
 */
public enum ParkingOrderType {
	RECHARGE((byte)1), OPEN_CARD((byte)2);

    private byte code;


	private ParkingOrderType(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static ParkingOrderType fromCode(Byte code) {
        if(code != null) {
            ParkingOrderType[] values = ParkingOrderType.values();
            for(ParkingOrderType value : values) {
                if(value.code == code.byteValue()) {
                    return value;
                }
            }
        }
        
        return null;
    }
}
