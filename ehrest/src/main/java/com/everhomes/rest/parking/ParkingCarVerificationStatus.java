// @formatter:off
package com.everhomes.rest.parking;

/**
 * <ul>停车充值 卡状态
 * <li>FAILED(1): 认证失败</li>
 * <li>AUDITING(2): 审核中</li>
 * <li>SUCCEED(3): 已认证</li>
 * </ul>
 */
public enum ParkingCarVerificationStatus {
    FAILED((byte)0), AUDITING((byte)1), SUCCEED((byte)2), INACTIVE((byte)3);

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
