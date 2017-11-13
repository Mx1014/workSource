// @formatter:off
package com.everhomes.rest.parking;

/**
 * <ul>停车充值月卡申请状态
 * <li>UN_AUTHORIZED(1): 未认证车牌绑定</li>
 * <li>AUTHORIZED(2): 认证申请</li>
 * </ul>
 */
public enum ParkingCarVerificationType {
    UN_AUTHORIZED((byte)1), AUTHORIZED((byte)2), IGNORE_REPEAT_UN_AUTHORIZED((byte)3);

    private byte code;

    private ParkingCarVerificationType(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static ParkingCarVerificationType fromCode(Byte code) {
        if(code != null) {
            ParkingCarVerificationType[] values = ParkingCarVerificationType.values();
            for(ParkingCarVerificationType value : values) {
                if(value.code == code.byteValue()) {
                    return value;
                }
            }
        }
        
        return null;
    }
}
