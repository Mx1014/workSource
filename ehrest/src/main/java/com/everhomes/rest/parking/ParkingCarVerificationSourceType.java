// @formatter:off
package com.everhomes.rest.parking;

/**
 * <ul>停车充值月卡申请状态
 * <li>CARD_REQUEST(1): 月卡申请</li>
 * <li>CAR_VERIFICATION(2): 车辆认证申请</li>
 * </ul>
 */
public enum ParkingCarVerificationSourceType {
    CARD_REQUEST((byte)1), CAR_VERIFICATION((byte)2);

    private byte code;

    private ParkingCarVerificationSourceType(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }

    public static ParkingCarVerificationSourceType fromCode(Byte code) {
        if(code != null) {
            ParkingCarVerificationSourceType[] values = ParkingCarVerificationSourceType.values();
            for(ParkingCarVerificationSourceType value : values) {
                if(value.code == code.byteValue()) {
                    return value;
                }
            }
        }
        
        return null;
    }
}
