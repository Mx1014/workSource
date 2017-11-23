// @formatter:off
package com.everhomes.rest.parking;

/**
 * <ul>
 * <li>MONTHLY(1): 月卡充值</li>
 * <li>TEMPORARY(2): 临时车缴费</li>
 * </ul>
 */
public enum ParkingRechargeType {
	MONTHLY((byte)1, "月卡充值"), TEMPORARY((byte)2, "临时车缴费");
    
    private byte code;
    private String describe;
    
    public String getDescribe() {
		return describe;
	}

	private ParkingRechargeType(byte code, String describe) {
        this.code = code;
        this.describe = describe;
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
