// @formatter:off
package com.everhomes.rest.parking;

/**
 * <ul>停车充值 卡状态
 * <li>FAILED(0): 认证失败</li>
 * <li>AUDITING(1): 审核中</li>
 * <li>SUCCEED(2): 已认证</li>
 * <li>INACTIVE(3): 无效</li>
 * <li>UN_AUTHORIZED(4): 未认证</li>
 * </ul>
 */
public enum ParkingCarVerificationStatus {
    FAILED((byte)0), AUDITING((byte)1), SUCCEED((byte)2), INACTIVE((byte)3), UN_AUTHORIZED((byte)4);

    private byte code;

    private ParkingCarVerificationStatus(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static ParkingCarVerificationStatus fromCode(Byte code) {
        if(code != null) {
            ParkingCarVerificationStatus[] values = ParkingCarVerificationStatus.values();
            for(ParkingCarVerificationStatus value : values) {
                if(value.code == code.byteValue()) {
                    return value;
                }
            }
        }
        
        return null;
    }
}
