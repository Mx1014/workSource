// @formatter:off
package com.everhomes.rest.parking;

/**
 * <ul>停车充值订单的充值状态
 * <li>NONE(0): 无效</li>
 * <li>UNRECHARGED(1): 未充值</li>
 * <li>RECHARGED(2): 已充值</li>
 * </ul>
 */
@Deprecated
public enum ParkingRechargeOrderRechargeStatus {
    NONE((byte)0, "无效"), UNRECHARGED((byte)1, "未充值"), RECHARGED((byte)2, "已充值");
    
    private byte code;
    private String describe;
    
    private ParkingRechargeOrderRechargeStatus(byte code, String describe) {
        this.code = code;
        this.describe = describe;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static ParkingRechargeOrderRechargeStatus fromCode(Byte code) {
        if(code != null) {
            ParkingRechargeOrderRechargeStatus[] values = ParkingRechargeOrderRechargeStatus.values();
            for(ParkingRechargeOrderRechargeStatus value : values) {
                if(value.code == code.byteValue()) {
                    return value;
                }
            }
        }
        
        return null;
    }

	public String getDescribe() {
		return describe;
	}

}
